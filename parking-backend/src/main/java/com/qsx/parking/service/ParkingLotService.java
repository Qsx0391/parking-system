package com.qsx.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsx.parking.dao.entity.ParkingLotDO;
import com.qsx.parking.dto.req.ParkingLotConfigUpdateReqDTO;
import com.qsx.parking.dto.resp.ParkingLotConfigRespDTO;
import com.qsx.parking.framework.result.Result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 停车场服务接口 | 提供停车场配置管理、费用计算和车位管理等功能
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public interface ParkingLotService extends IService<ParkingLotDO> {

    /**
     * 获取停车场配置信息
     * @return 包含停车场配置信息的统一返回结果
     */
    ParkingLotConfigRespDTO getParkingConfig();

    /**
     * 更新停车场配置信息
     * @param requestParam 请求参数
     */
    void updateParkingConfig(ParkingLotConfigUpdateReqDTO requestParam);

    /**
     * 计算停车费用
     * @param enteredAt 入场时间
     * @param exitedAt  出场时间
     * @return 停车费用
     */
    BigDecimal getFee(LocalDateTime enteredAt, LocalDateTime exitedAt);

    /**
     * 检查并预留停车位
     * @return 是否成功预留车位
     */
    boolean checkAndReserveSpace();

    /**
     * 释放一个停车位
     */
    void releaseSpace();

}
