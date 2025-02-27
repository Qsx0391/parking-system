package com.qsx.parking.controller;

import com.qsx.parking.dto.req.LogRecordPageQueryReqDTO;
import com.qsx.parking.framework.result.Result;
import com.qsx.parking.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志管理控制层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "操作日志管理")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @Operation(summary = "分页查询操作日志记录")
    @PostMapping("api/admin/operation_log/page")
    public Result<Object> pageQueryOperationLogRecord(@RequestBody LogRecordPageQueryReqDTO requestParam) {
        return Result.success(operationLogService.pageQueryOperationLogRecord(requestParam));
    }
}
