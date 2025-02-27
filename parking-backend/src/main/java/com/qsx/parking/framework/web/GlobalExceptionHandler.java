package com.qsx.parking.framework.web;

import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.exception.AbstractException;
import com.qsx.parking.framework.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常处理器
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstError = bindingResult.getFieldError();
        String message = Optional.ofNullable(firstError)
                .map(FieldError::getDefaultMessage)
                .orElse("");
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), message);
        return Result.error(ErrorCodeEnum.CLIENT_ERROR.code(), message);
    }

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(AbstractException.class)
    public Result<Void> abstractException(HttpServletRequest request, AbstractException ex) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), ex, ex.getCause());
            return Result.error(ex);
        }
        StringBuilder stackTraceBuilder = new StringBuilder();
        stackTraceBuilder.append(ex.getClass().getName()).append(": ").append(ex.getErrorMessage()).append("\n");
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int i = 0; i < Math.min(5, stackTrace.length); i++) {
            stackTraceBuilder.append("\tat ").append(stackTrace[i]).append("\n");
        }
        log.error("[{}] {} [ex] {} \n\n{}", request.getMethod(), getUrl(request), ex, stackTraceBuilder);
        return Result.error(ex);
    }

    /**
     * 拦截未知异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<Void> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        return Result.error(ErrorCodeEnum.SERVICE_ERROR);
    }

    /**
     * 获取请求URL
     */
    private String getUrl(HttpServletRequest request) {
        if (StringUtils.hasLength(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
