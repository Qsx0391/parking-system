package com.qsx.parking.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.qsx.parking.common.context.UserContext;
import com.qsx.parking.common.context.UserInfoDTO;
import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.exception.ClientException;
import com.qsx.parking.framework.result.Result;
import com.qsx.parking.utils.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录验证过滤器 | 用于验证用户是否已登录，拦截未登录用户的请求
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            Result<Object> result = Result.error(ErrorCodeEnum.IDEMPOTENT_TOKEN_NULL_ERROR);
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        UserInfoDTO userInfo;
        if (jwtHelper.isExpiration(token) || (userInfo = jwtHelper.getUserInfo(token)) == null) {
            Result<Object> result = Result.error(ErrorCodeEnum.IDEMPOTENT_TOKEN_DELETE_ERROR);
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        UserContext.setUser(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
