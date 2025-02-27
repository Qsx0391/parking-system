package com.qsx.parking.utils;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 车牌工具类 | 用于校验车牌是否合法
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public class LicensePlateUtil {

    /**
     * 正则表达式，匹配中国车牌（例如：京A12345）
     */
    private static final String LICENSE_PLATE_REGEX =
            "(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|" +
            "([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
            "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))";

    /**
     * 校验车牌是否合法
     * @param licensePlate 车牌号
     * @return 车牌是否合法
     */
    public static boolean isInvalidLicensePlate(String licensePlate) {
        return StrUtil.isEmpty(licensePlate) || !Pattern.matches(LICENSE_PLATE_REGEX, licensePlate);
    }
}