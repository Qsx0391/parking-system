package com.qsx.parking.framework.result;

import com.qsx.parking.framework.errorcode.IErrorCode;
import com.qsx.parking.framework.exception.AbstractException;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局返回对象
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@Accessors(chain = true)
public class Result<T> {

    private final static String SUCCESS_CODE = "0";

    private String code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "success");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "success", data);
    }

    public static <T> Result<T> error(String code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> error(IErrorCode errorCode) {
        return new Result<>(errorCode.code(), errorCode.message());
    }

    public static <T> Result<T> error(AbstractException exception) {
        return new Result<>(exception.errorCode, exception.errorMessage);
    }

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
