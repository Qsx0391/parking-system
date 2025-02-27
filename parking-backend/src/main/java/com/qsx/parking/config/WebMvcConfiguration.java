package com.qsx.parking.config;

import com.qsx.parking.config.filter.CorsFilter;
import com.qsx.parking.config.filter.FilterOrderConstant;
import com.qsx.parking.config.filter.RepeatableFilter;
import com.qsx.parking.framework.web.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebMvc 配置类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration {

    /**
     * 构建跨域过滤器
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.setName("CorsFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    /**
     * 构建重复读取请求体过滤器
     */
    @Bean
    public FilterRegistrationBean<RepeatableFilter> repeatableFilter() {
        FilterRegistrationBean<RepeatableFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RepeatableFilter());
        registrationBean.setName("RepeatableFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(FilterOrderConstant.REPEATABLE_FILTER);
        return registrationBean;
    }

}
