package com.qsx.parking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsx.parking.common.context.UserContext;
import com.qsx.parking.common.enums.PaymentStatusEnum;
import com.qsx.parking.common.enums.VehicleStatusEnum;
import com.qsx.parking.dao.mapper.PaymentMapper;
import com.qsx.parking.dao.mapper.VehicleMapper;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.dao.entity.VehicleDO;
import com.qsx.parking.delay.DelayCancelPaymentManager;
import com.qsx.parking.delay.DelayCancelPaymentTask;
import com.qsx.parking.dto.req.PaymentPageQueryReqDTO;
import com.qsx.parking.dto.resp.PaymentInfoRespDTO;
import com.qsx.parking.dto.resp.PaymentPageQueryRespDTO;
import com.qsx.parking.framework.exception.ClientException;
import com.qsx.parking.framework.page.OrderLambdaPageQueryWrapper;
import com.qsx.parking.framework.page.PageQueryRespDTO;
import com.qsx.parking.service.ParkingLotService;
import com.qsx.parking.service.PaymentService;
import com.qsx.parking.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, PaymentDO> implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final VehicleMapper vehicleMapper;
    private final ParkingLotService parkingLotService;
    private final DelayCancelPaymentManager delayCancelPaymentManager;

    @Setter
    @Value("${parking.exitTimeOut:}")
    int exitTimeOut;

    @Override
    @Transactional
    public PaymentInfoRespDTO createOfflinePayment(String licensePlate) {
        if (LicensePlateUtil.isInvalidLicensePlate(licensePlate)) {
            throw new ClientException("请检查车牌号格式是否正确");
        }

        LambdaQueryWrapper<VehicleDO> vehicleWrapper = Wrappers.lambdaQuery(VehicleDO.class)
                .eq(VehicleDO::getVno, licensePlate)
                .eq(!UserContext.isAdministrator(), VehicleDO::getUid, UserContext.getUserId())
                .ne(VehicleDO::getStatus, VehicleStatusEnum.LEFT.getType());
        VehicleDO vehicleDO = vehicleMapper.selectOne(vehicleWrapper);

        if (vehicleDO == null) {
            throw new ClientException("车辆不存在");
        }
        if (vehicleDO.getStatus() == VehicleStatusEnum.COMPLETED.getType()) {
            throw new ClientException("车辆已支付");
        }

        PaymentInfoRespDTO paymentInfoRespDTO = PaymentInfoRespDTO.builder()
                .vid(String.valueOf(vehicleDO.getId()))
                .enteredAt(vehicleDO.getEnteredAt())
                .build();

        PaymentDO paymentDO;
        if (vehicleDO.getStatus() == VehicleStatusEnum.PROCESSING.getType()) {
            // 如果车辆状态为处理中，查询支付订单
            LambdaQueryWrapper<PaymentDO> paymentWrapper = Wrappers.lambdaQuery(PaymentDO.class)
                    .eq(PaymentDO::getVid, vehicleDO.getId())
                    .and(wq -> wq.eq(PaymentDO::getStatus, PaymentStatusEnum.PENDING.getType())
                            .or().eq(PaymentDO::getStatus, PaymentStatusEnum.PROCESSING.getType())
                            .or().eq(PaymentDO::getStatus, PaymentStatusEnum.COMPLETED.getType()));
            paymentDO = paymentMapper.selectOne(paymentWrapper);

            if (paymentDO.getStatus() == PaymentStatusEnum.PROCESSING.getType()) {
                throw new ClientException("支付处理中");
            }
            if (paymentDO.getStatus() == PaymentStatusEnum.COMPLETED.getType()) {
                throw new ClientException("支付已完成");
            }
        } else {
            // 否则创建支付订单
            paymentDO = PaymentDO.builder()
                    .vid(vehicleDO.getId())
                    .vno(vehicleDO.getVno())
                    .uid(UserContext.getUserId())
                    .amount(parkingLotService.getFee(vehicleDO.getEnteredAt(), LocalDateTime.now()))
                    .method("offline")
                    .status(PaymentStatusEnum.PENDING.getType())
                    .build();
            paymentMapper.insert(paymentDO);

            VehicleDO updateVehicleDO = VehicleDO.builder()
                    .id(vehicleDO.getId())
                    .status(VehicleStatusEnum.PROCESSING.getType())
                    .fee(paymentDO.getAmount())
                    .build();
            vehicleMapper.updateById(updateVehicleDO);

            // 添加延迟取消支付任务
            DelayCancelPaymentTask delayTask = DelayCancelPaymentTask.builder()
                    .pid(paymentDO.getId())
                    .expire(paymentDO.getCreatedAt().plusMinutes(exitTimeOut).toInstant(ZoneOffset.of("+8")).toEpochMilli())
                    .build();
            delayCancelPaymentManager.addTask(delayTask);
        }

        paymentInfoRespDTO.setFee(paymentDO.getAmount());
        paymentInfoRespDTO.setPid(String.valueOf(paymentDO.getId()));
        paymentInfoRespDTO.setPaymentCreateAt(paymentDO.getCreatedAt());
        return paymentInfoRespDTO;
    }

    @Override
    public void cancelPayment(Long vid, Long pid) {
        LambdaQueryWrapper<VehicleDO> vehicleWrapper = Wrappers.lambdaQuery(VehicleDO.class)
                .eq(VehicleDO::getId, vid)
                .eq(!UserContext.isAdministrator(), VehicleDO::getUid, UserContext.getUserId());
        VehicleDO vehicleDO = vehicleMapper.selectOne(vehicleWrapper);

        LambdaQueryWrapper<PaymentDO> paymentWrapper = Wrappers.lambdaQuery(PaymentDO.class)
                .eq(PaymentDO::getId, pid)
                .eq(PaymentDO::getVid, vid);
        PaymentDO paymentDO = paymentMapper.selectOne(paymentWrapper);

        checkPendingPayment(paymentDO, vehicleDO);

        LambdaUpdateWrapper<VehicleDO> updateWrapper = Wrappers.lambdaUpdate(VehicleDO.class)
                .eq(VehicleDO::getId, vid)
                .set(VehicleDO::getStatus, VehicleStatusEnum.ENTERED.getType())
                .set(VehicleDO::getExitedAt, null);
        vehicleMapper.update(null, updateWrapper);

        PaymentDO updatePaymentDO = PaymentDO.builder()
                .id(pid)
                .status(PaymentStatusEnum.CANCELED.getType())
                .build();
        paymentMapper.updateById(updatePaymentDO);
    }

    @Override
    public void payOffline(Long vid, Long pid) {
        LambdaQueryWrapper<VehicleDO> vehicleWrapper = Wrappers.lambdaQuery(VehicleDO.class)
                .eq(VehicleDO::getId, vid)
                .eq(!UserContext.isAdministrator(), VehicleDO::getUid, UserContext.getUserId());
        VehicleDO vehicleDO = vehicleMapper.selectOne(vehicleWrapper);

        LambdaQueryWrapper<PaymentDO> paymentWrapper = Wrappers.lambdaQuery(PaymentDO.class)
                .eq(PaymentDO::getId, pid)
                .eq(PaymentDO::getVid, vid);
        PaymentDO paymentDO = paymentMapper.selectOne(paymentWrapper);

        checkPendingPayment(paymentDO, vehicleDO);

        PaymentDO updatePayment = PaymentDO.builder()
                .id(pid)
                .status(PaymentStatusEnum.COMPLETED.getType())
                .paidAt(LocalDateTime.now())
                .method("offline")
                .build();
        paymentMapper.updateById(updatePayment);

        VehicleDO updateVehicle = VehicleDO.builder()
                .id(vid)
                .status(VehicleStatusEnum.COMPLETED.getType())
                .paidAt(LocalDateTime.now())
                .build();
        vehicleMapper.updateById(updateVehicle);
    }

    @Override
    public PageQueryRespDTO<Object> pageQueryPaymentRecord(PaymentPageQueryReqDTO requestParam) {
        OrderLambdaPageQueryWrapper<PaymentDO> wrapper = new OrderLambdaPageQueryWrapper<>();
        wrapper.orderBy(requestParam.getOrders(), PaymentDO.class);
        wrapper.eq(!UserContext.isAdministrator(), PaymentDO::getUid, UserContext.getUserId());
        wrapper.eq(StrUtil.isNotBlank(requestParam.getVno()), PaymentDO::getVno, requestParam.getVno());
        wrapper.eq(Objects.nonNull(requestParam.getStatus()), PaymentDO::getStatus, requestParam.getStatus());
        wrapper.between(Objects.nonNull(requestParam.getStartedAt()) && Objects.nonNull(requestParam.getEndedAt()),
                PaymentDO::getCreatedAt, requestParam.getStartedAt(), requestParam.getEndedAt());

        IPage<PaymentDO> page = paymentMapper.selectPage(requestParam.getPage(), wrapper);
        return PageQueryRespDTO.of(page.convert(entity -> BeanUtil.toBean(entity, PaymentPageQueryRespDTO.class)));
    }

    private void checkPendingPayment(PaymentDO paymentDO, VehicleDO vehicleDO) {
        if (vehicleDO == null) {
            throw new ClientException("车辆不存在");
        }
        if (vehicleDO.getStatus() == VehicleStatusEnum.COMPLETED.getType()) {
            throw new ClientException("车辆已支付");
        }
        if (vehicleDO.getStatus() == VehicleStatusEnum.LEFT.getType()) {
            throw new ClientException("车辆已离场");
        }

        if (paymentDO == null) {
            throw new ClientException("支付订单不存在");
        }
        if (paymentDO.getStatus() != PaymentStatusEnum.PENDING.getType()) {
            throw new ClientException("支付订单状态异常");
        }
    }
}
