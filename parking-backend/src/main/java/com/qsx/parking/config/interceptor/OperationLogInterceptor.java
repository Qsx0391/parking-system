package com.qsx.parking.config.interceptor;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.qsx.parking.common.context.UserContext;
import com.qsx.parking.dao.mapper.OperationLogMapper;
import com.qsx.parking.dao.entity.OperationLogDO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 操作日志过滤器 | 记录用户的操作请求和响应信息
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RequiredArgsConstructor
public class OperationLogInterceptor implements HandlerInterceptor {

    private final OperationLogMapper operationLogMapper;

    private static final ThreadLocal<OperationLogDO> OPERATION_LOG_DTO_THREAD_LOCAL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String operation = request.getRequestURI().replaceFirst("/api", "");

        String token = request.getHeader("token");
        String params = request.getQueryString();
        if (StrUtil.isEmpty(params)) {
            params = IoUtil.read(request.getReader());
        }

        OperationLogDO operationLogDTO = OperationLogDO.builder()
                .operation(operation)
                .params(params)
                .ip(request.getRemoteAddr())
                .uid(UserContext.getUserId())
                .duration(System.currentTimeMillis())
                .build();

        OPERATION_LOG_DTO_THREAD_LOCAL.set(operationLogDTO);
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        OperationLogDO operationLogDTO = OPERATION_LOG_DTO_THREAD_LOCAL.get();
        operationLogDTO.setDuration(System.currentTimeMillis() - operationLogDTO.getDuration());
        operationLogMapper.insert(operationLogDTO);
    }
}
