package com.qsx.parking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsx.parking.common.context.UserContext;
import com.qsx.parking.common.enums.VehicleStatusEnum;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.dao.mapper.PaymentMapper;
import com.qsx.parking.dao.mapper.VehicleMapper;
import com.qsx.parking.dao.entity.VehicleDO;
import com.qsx.parking.dto.req.ParkingRecordPageQueryReqDTO;
import com.qsx.parking.dto.req.ParkingRecordUpdateReqDTO;
import com.qsx.parking.dto.resp.ParkingRecordQueryRespDTO;
import com.qsx.parking.framework.exception.ClientException;
import com.qsx.parking.framework.page.OrderLambdaPageQueryWrapper;
import com.qsx.parking.framework.page.PageQueryRespDTO;
import com.qsx.parking.service.ParkingLotService;
import com.qsx.parking.service.VehicleService;
import com.qsx.parking.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, VehicleDO> implements VehicleService {

    private final ParkingLotService parkingLotService;
    private final VehicleMapper vehicleMapper;

    @Override
    public void vehicleEntry(String licensePlate) {
        if (LicensePlateUtil.isInvalidLicensePlate(licensePlate)) {
            throw new ClientException("请检查车牌号格式是否正确");
        }

        // 判断车辆是否已进入
        LambdaQueryWrapper<VehicleDO> wrapper = Wrappers.lambdaQuery(VehicleDO.class)
                .eq(VehicleDO::getVno, licensePlate)
                .ne(VehicleDO::getStatus, VehicleStatusEnum.LEFT.getType());
        if (vehicleMapper.selectCount(wrapper) != 0) {
            throw new ClientException("车辆已进入");
        }

        // 判断车位是否已满，预约车位
        if (!parkingLotService.checkAndReserveSpace()) {
            throw new ClientException("车位已满");
        }

        VehicleDO vehicleDO = VehicleDO.builder()
                .vno(licensePlate)
                .uid(UserContext.getUserId())
                .enteredAt(LocalDateTime.now())
                .status(VehicleStatusEnum.ENTERED.getType())
                .build();
        vehicleMapper.insert(vehicleDO);
    }

    @Override
    @Transactional
    public void vehicleExit(String licensePlate) {
        if (LicensePlateUtil.isInvalidLicensePlate(licensePlate)) {
            throw new ClientException("请检查车牌号格式是否正确");
        }

        LambdaQueryWrapper<VehicleDO> vehicleWrapper = Wrappers.lambdaQuery(VehicleDO.class)
                .eq(VehicleDO::getVno, licensePlate)
                .ne(VehicleDO::getStatus, VehicleStatusEnum.LEFT.getType());
        VehicleDO vehicleDO = vehicleMapper.selectOne(vehicleWrapper);

        if (vehicleDO == null) {
            throw new ClientException("车辆不存在");
        }
        if (vehicleDO.getStatus() != VehicleStatusEnum.COMPLETED.getType()) {
            throw new ClientException("车辆未支付");
        }

        parkingLotService.releaseSpace();
        VehicleDO updateVehicle = VehicleDO.builder()
                .id(vehicleDO.getId())
                .status(VehicleStatusEnum.LEFT.getType())
                .exitedAt(LocalDateTime.now())
                .build();
        vehicleMapper.updateById(updateVehicle);
    }

    @Override
    public PageQueryRespDTO<Object> pageQueryParkingRecord(ParkingRecordPageQueryReqDTO requestParam) {
        OrderLambdaPageQueryWrapper<VehicleDO> wrapper = new OrderLambdaPageQueryWrapper<>();
        wrapper.orderBy(requestParam.getOrders(), VehicleDO.class);
        wrapper.eq(!UserContext.isAdministrator(), VehicleDO::getUid, UserContext.getUserId());
        wrapper.eq(StrUtil.isNotBlank(requestParam.getVno()), VehicleDO::getVno, requestParam.getVno());
        wrapper.eq(Objects.nonNull(requestParam.getStatus()), VehicleDO::getStatus, requestParam.getStatus());
        wrapper.between(Objects.nonNull(requestParam.getStartedAt()) && Objects.nonNull(requestParam.getEndedAt()),
                VehicleDO::getEnteredAt, requestParam.getStartedAt(), requestParam.getEndedAt());

        IPage<VehicleDO> page = vehicleMapper.selectPage(requestParam.getPage(), wrapper);
        return PageQueryRespDTO.of(page.convert(entity -> BeanUtil.toBean(entity, ParkingRecordQueryRespDTO.class)));
    }

    @Override
    public void updateParkingRecord(ParkingRecordUpdateReqDTO requestParam) {
        VehicleDO vehicleDO = BeanUtil.toBean(requestParam, VehicleDO.class);
        vehicleMapper.updateById(vehicleDO);
    }
}
