package com.qsx.parking.controller;

import com.qsx.parking.dto.req.ParkingRecordPageQueryReqDTO;
import com.qsx.parking.dto.req.ParkingRecordUpdateReqDTO;
import com.qsx.parking.service.VehicleService;
import com.qsx.parking.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 停车场车辆管理控制层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "车辆管理")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "车辆入场")
    @PostMapping("api/vehicle/entry")
    public Result<Object> vehicleEntry(String licensePlate) {
        vehicleService.vehicleEntry(licensePlate);
        return Result.success();
    }

    @Operation(summary = "车辆离场")
    @PostMapping("api/vehicle/exit")
    public Result<Object> vehicleExit(String licensePlate) {
        vehicleService.vehicleExit(licensePlate);
        return Result.success();
    }

    @Operation(summary = "分页查询停车记录")
    @PostMapping("api/vehicle/record/page")
    public Result<Object> pageQueryParkingRecord(@RequestBody ParkingRecordPageQueryReqDTO requestParam) {
        return Result.success(vehicleService.pageQueryParkingRecord(requestParam));
    }

    @Operation(summary = "修改停车记录")
    @PostMapping("api/admin/vehicle/record/update")
    public Result<Object> updateParkingRecord(@RequestBody @Valid ParkingRecordUpdateReqDTO requestParam) {
        vehicleService.updateParkingRecord(requestParam);
        return Result.success();
    }
}
