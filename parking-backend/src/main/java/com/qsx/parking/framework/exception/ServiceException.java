package com.qsx.parking.framework.exception;

import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.errorcode.IErrorCode;

/**
 * 服务端异常
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public class ServiceException extends AbstractException {

    public ServiceException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ServiceException(String message) {
        this(message, null, ErrorCodeEnum.SERVICE_ERROR);
    }

    public ServiceException(String message, Throwable throwable) {
        this(message, throwable, ErrorCodeEnum.SERVICE_ERROR);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
