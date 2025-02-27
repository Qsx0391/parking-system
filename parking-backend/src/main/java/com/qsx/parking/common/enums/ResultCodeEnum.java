package com.qsx.parking.common.enums;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "success"),
    USERNAME_ERROR(501, "username error"),
    PASSWORD_ERROR(502, "password error"),
    NO_LOGIN(503, "no login"),
    USER_NOT_EXIST(504, "user not exist"),
    USERNAME_USED(505, "username used"),
    VEHICLE_ENTRY_EXIST(506, "vehicle has entered"),
    INVALID_LICENSE_PLATE(507, "invalid license plate"),
    VEHICLE_NOT_EXIST(508, "vehicle not exist"),
    PARKING_FULL(509, "parking full"),
    PAYMENT_NOT_EXIST(510, "payment not exist"),
    COMPLETED(511, "payment completed"),
    ALREADY_PAID(512, "already paid"),
    PAYMENT_PROCESSING(513, "payment processing"),
    NOT_PROCESSING(514, "not processing"),
    NOT_COMPLETED(515, "not completed"),
    LEAVE_TIMEOUT(516, "leave timeout");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
