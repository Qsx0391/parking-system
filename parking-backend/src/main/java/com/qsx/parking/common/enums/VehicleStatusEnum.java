package com.qsx.parking.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 停车车辆状态枚举
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Getter
@AllArgsConstructor
public enum VehicleStatusEnum {

    /**
     * 已进入
     */
    ENTERED(0),

    /**
     * 支付中
     */
    PROCESSING(1),

    /**
     * 已支付
     */
    COMPLETED(2),

    /**
     * 已离场
     */
    LEFT(3);

    private final int type;
}
