package com.qsx.parking.controller;

import com.qsx.parking.dto.req.PaymentPageQueryReqDTO;
import com.qsx.parking.service.PaymentService;
import com.qsx.parking.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支付管理控制层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "支付管理")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "创建支付")
    @GetMapping("api/payment/create/offline")
    public Result<Object> createOfflinePayment(String licensePlate){
        return Result.success(paymentService.createOfflinePayment(licensePlate));
    }

    @Operation(summary = "取消支付")
    @PostMapping("api/payment/cancel")
    public Result<Object> cancelPayment(Long vid, Long pid) {
        paymentService.cancelPayment(vid, pid);
        return Result.success();
    }

    @Operation(summary = "处理线下支付")
    @PostMapping("api/payment/pay/offline")
    public Result<Object> payOffline(Long vid, Long pid) {
        paymentService.payOffline(vid, pid);
        return Result.success();
    }

    @Operation(summary = "分页查询支付记录")
    @PostMapping("api/payment/record/page")
    public Result<Object> pageQueryPaymentRecord(@RequestBody PaymentPageQueryReqDTO requestParam) {
        return Result.success(paymentService.pageQueryPaymentRecord(requestParam));
    }
}
