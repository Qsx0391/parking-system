package com.qsx.parking.framework.exception;

import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.errorcode.IErrorCode;

/**
 * 远程服务调用异常
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public class RemoteException extends AbstractException {

    public RemoteException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public RemoteException(String message) {
        this(message, null, ErrorCodeEnum.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
