package com.qsx.parking.framework.exception;

import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.errorcode.IErrorCode;

/**
 * 客户端异常
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public class ClientException extends AbstractException {

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, ErrorCodeEnum.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
