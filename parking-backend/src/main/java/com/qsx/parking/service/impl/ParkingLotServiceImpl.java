package com.qsx.parking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsx.parking.dao.mapper.ParkingLotMapper;
import com.qsx.parking.dao.entity.ParkingLotDO;
import com.qsx.parking.dto.req.ParkingLotConfigUpdateReqDTO;
import com.qsx.parking.dto.resp.ParkingLotConfigRespDTO;
import com.qsx.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl extends ServiceImpl<ParkingLotMapper, ParkingLotDO> implements ParkingLotService {

    private final ParkingLotMapper parkingLotMapper;

    private final static int PARKING_LOT_CONFIG_ID = 1;

    @Override
    public ParkingLotConfigRespDTO getParkingConfig() {
        ParkingLotDO parkingLotDO = parkingLotMapper.selectById(PARKING_LOT_CONFIG_ID);
        return BeanUtil.toBean(parkingLotDO, ParkingLotConfigRespDTO.class);
    }

    @Override
    @Transactional
    public void updateParkingConfig(ParkingLotConfigUpdateReqDTO requestParam) {
        if (requestParam.getBillingUnit() != null && requestParam.getBillingUnit() <= 0) {
            throw new IllegalArgumentException("计费单元必须大于0");
        }
        if (requestParam.getFreeTime() != null && requestParam.getFreeTime() < 0) {
            throw new IllegalArgumentException("免费时长不能为负数");
        }
        if (requestParam.getMaxPrice() != null && requestParam.getMaxPrice() < 0) {
            throw new IllegalArgumentException("最大费用不能为负数");
        }
        if (requestParam.getUnitPrice() != null && requestParam.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("计费单价不能为负数");
        }

        ParkingLotDO parkingLotDO = BeanUtil.toBean(requestParam, ParkingLotDO.class);
        if (parkingLotDO.getTotalSpaces() != null) {
            ParkingLotDO prevConfig = getById(PARKING_LOT_CONFIG_ID);
            int currentSpaces = prevConfig.getCurrentSpaces() - prevConfig.getTotalSpaces() + parkingLotDO.getTotalSpaces();
            if (currentSpaces < 0) {
                throw new IllegalArgumentException("车位数不能小于已停车辆数");
            }
            parkingLotDO.setCurrentSpaces(currentSpaces);
        }

        parkingLotDO.setId(PARKING_LOT_CONFIG_ID);
        parkingLotMapper.updateById(parkingLotDO);
    }

    @Override
    public BigDecimal getFee(LocalDateTime enteredAt, LocalDateTime exitedAt) {
        ParkingLotDO config = parkingLotMapper.selectById(PARKING_LOT_CONFIG_ID);
        int freeTime = config.getFreeTime();
        int billingUnit = config.getBillingUnit();
        long parkingMinutes = Duration.between(enteredAt, exitedAt).toMinutes();

        // 如果停车免费时长内，则免费
        if (parkingMinutes <= freeTime) {
            return BigDecimal.ZERO;
        }
        long unitsCount = (parkingMinutes + billingUnit - 1 - freeTime) / billingUnit;
        BigDecimal fee = config.getUnitPrice().multiply(BigDecimal.valueOf(unitsCount));

        // 如果停车费用超过最大费用，则按最大费用计算
        if (fee.compareTo(BigDecimal.valueOf(config.getMaxPrice())) > 0) {
            fee = BigDecimal.valueOf(config.getMaxPrice());
        }
        return fee;
    }

    @Override
    public boolean checkAndReserveSpace() {
        return parkingLotMapper.checkAndReserveSpace() == 1;
    }

    @Override
    public void releaseSpace() {
        parkingLotMapper.releaseSpace();
    }

}
