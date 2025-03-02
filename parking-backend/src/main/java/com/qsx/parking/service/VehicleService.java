package com.qsx.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsx.parking.dao.entity.VehicleDO;
import com.qsx.parking.dto.req.ParkingRecordPageQueryReqDTO;
import com.qsx.parking.dto.req.ParkingRecordUpdateReqDTO;
import com.qsx.parking.dto.resp.PaymentInfoRespDTO;
import com.qsx.parking.framework.page.PageQueryRespDTO;
import com.qsx.parking.framework.result.Result;
import jakarta.validation.Valid;

/**
 * 车辆服务逻辑层 | 处理车辆进出场和状态管理等相关业务
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public interface VehicleService extends IService<VehicleDO> {

    /**
     * 处理车辆入场
     * @param licensePlate 车牌号
     */
    void vehicleEntry(String licensePlate);

    /**
     * 处理车辆离场
     * @param licensePlate 车牌号
     */
    void vehicleExit(String licensePlate);

    /**
     * 分页查询车辆停车记录
     * @param requestParam 请求参数
     * @return 分页查询结果
     */
    PageQueryRespDTO<Object> pageQueryParkingRecord(ParkingRecordPageQueryReqDTO requestParam);

    /**
     * 修改停车记录
     * @param requestParam 请求参数
     */
    void updateParkingRecord(@Valid ParkingRecordUpdateReqDTO requestParam);
}
