package com.qsx.parking.config;

import com.qsx.parking.config.interceptor.AdminInterceptor;
import com.qsx.parking.config.interceptor.InterceptorOrderConstant;
import com.qsx.parking.config.interceptor.LoginInterceptor;
import com.qsx.parking.utils.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用户相关配置类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Configuration
@RequiredArgsConstructor
public class UserConfiguration implements WebMvcConfigurer {

    private final JwtHelper jwtHelper;

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(jwtHelper);
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    /**
     * 构建登录拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns(
                        "/api/admin/**",
                        "/api/config/**",
                        "/api/payment/**",
                        "/api/vehicle/**"
                )
                .order(InterceptorOrderConstant.LOGIN_INTERCEPTOR);
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/api/admin/**")
                .order(InterceptorOrderConstant.ADMIN_INTERCEPTOR);
    }

}
