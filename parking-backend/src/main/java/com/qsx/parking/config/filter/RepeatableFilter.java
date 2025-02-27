package com.qsx.parking.config.filter;

import com.qsx.parking.utils.RepeatableHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 可重复读取请求体过滤器 | 包装HttpServletRequest，使请求体可以被多次读取
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public class RepeatableFilter implements Filter {

    /**
     * 执行过滤 | 将原始请求包装为可重复读取的请求对象
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest request = new RepeatableHttpServletRequest((HttpServletRequest) servletRequest);
        filterChain.doFilter(request, servletResponse);
    }
}
