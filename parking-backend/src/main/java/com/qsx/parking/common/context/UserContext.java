package com.qsx.parking.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.qsx.parking.common.enums.UserTypeEnum;

import java.util.Optional;

public class UserContext {

    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setUser(UserInfoDTO user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static UserInfoDTO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static Long getUserId() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    public static Integer getUserType() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserType).orElse(null);
    }

    public static boolean isAdministrator() {
        return Optional.ofNullable(getUserType()).map(type -> type == UserTypeEnum.ADMIN.getType()).orElse(false);
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}
