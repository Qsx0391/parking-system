package com.qsx.parking.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 管理员
     */
    ADMIN(0),

    /**
     * 普通用户
     */
    PARKING_USER(1);

    private final int type;
    }
