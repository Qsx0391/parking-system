package com.qsx.parking.config;

import com.qsx.parking.config.interceptor.InterceptorOrderConstant;
import com.qsx.parking.dao.mapper.OperationLogMapper;
import com.qsx.parking.config.interceptor.OperationLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 日志相关配置类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Configuration
@RequiredArgsConstructor
public class LogConfiguration implements WebMvcConfigurer {

    private final OperationLogMapper operationLogMapper;

    @Bean
    public OperationLogInterceptor operationLogInterceptor() {
        return new OperationLogInterceptor(operationLogMapper);
    }

    /**
     * 构建操作日志拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(operationLogInterceptor())
                .addPathPatterns(
                        "/api/vehicle/entry",
                        "/api/vehicle/exit",
                        "/api/payment/create",
                        "/api/payment/pay/offline",
                        "/api/admin/config/update"
                )
                .order(InterceptorOrderConstant.OPERATION_LOG_INTERCEPTOR);
    }
}
