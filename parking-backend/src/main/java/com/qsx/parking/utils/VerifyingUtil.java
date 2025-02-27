package com.qsx.parking.utils;

/**
 * 校验工具类 | 用于校验用户名、密码等信息是否合法
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public class VerifyingUtil {

    /**
     * 用户名校验：必须以字母开头，长度 5-20，仅包含字母、数字、下划线
     * @param username 用户名
     * @return 用户名是否合法
     */
    public static boolean checkUsername(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z][a-zA-Z0-9_]{4,19}$");
    }

    /**
     * 密码校验：长度 8-20，至少包含一个字母和一个数字
     * @param password 密码
     * @return 密码是否合法
     */
    public static boolean checkPassword(String password) {
        if (password == null) return false;
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$");
    }
}
