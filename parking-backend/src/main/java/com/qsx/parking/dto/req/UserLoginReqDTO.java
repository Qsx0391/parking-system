package com.qsx.parking.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 用户登录请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Getter
@Schema(description = "用户登录请求参数")
public class UserLoginReqDTO {

    @Schema(description = "登录用户名", example = "admin")
    @NotNull
    private String username;

    @Schema(description = "登录密码", example = "123456")
    @NotNull
    private String password;
}
