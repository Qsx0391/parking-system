package com.qsx.parking.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {

    /**
     * 待支付
     */
    PENDING(0),

    /**
     * 处理中
     */
    PROCESSING(1),

    /**
     * 已完成
     */
    COMPLETED(2),

    /**
     * 已取消
     */
    CANCELED(3),

    /**
     * 待退款
     */
    PENDING_REFUND(4),

    /**
     * 退款中
     */
    REFUNDING(5),

    /**
     * 已退款
     */
    REFUNDED(6);

    private final int type;
}
