package com.qsx.parking.framework.exception;

import com.qsx.parking.framework.errorcode.IErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 抽象项目中三类异常体系，客户端异常、服务端异常以及远程服务调用异常
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = StringUtils.hasLength(message) ? message : errorCode.message();
    }
}
