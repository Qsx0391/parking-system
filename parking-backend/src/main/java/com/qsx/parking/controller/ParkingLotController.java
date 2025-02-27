package com.qsx.parking.controller;


import com.qsx.parking.dto.req.ParkingLotConfigUpdateReqDTO;
import com.qsx.parking.service.ParkingLotService;
import com.qsx.parking.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 停车场配置管理控制层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "停车场配置管理")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Operation(summary = "获取停车场配置")
    @GetMapping("api/config/get")
    public Result<Object> getParkingInfo() {
        return Result.success(parkingLotService.getParkingConfig());
    }

    @Operation(summary = "更新停车场配置")
    @PostMapping("api/admin/config/update")
    public Result<Object> updateParkingConfig(@RequestBody ParkingLotConfigUpdateReqDTO requestParam) {
        parkingLotService.updateParkingConfig(requestParam);
        return Result.success();
    }
}
