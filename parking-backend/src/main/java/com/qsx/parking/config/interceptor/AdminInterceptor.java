package com.qsx.parking.config.interceptor;

import com.alibaba.fastjson2.JSON;
import com.qsx.parking.common.context.UserContext;
import com.qsx.parking.common.enums.UserTypeEnum;
import com.qsx.parking.framework.exception.ClientException;
import com.qsx.parking.framework.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (UserContext.getUserType() != UserTypeEnum.ADMIN.getType()) {
            Result<Object> result = Result.error(new ClientException("无权限访问"));
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        return true;
    }
}
