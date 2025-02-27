package com.qsx.parking.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 用户注册请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Getter
@Schema(description = "用户注册请求参数")
public class UserRegisterReqDTO {

    @Schema(description = "注册用户名", example = "admin")
    @NotNull
    private String username;

    @Schema(description = "注册密码", example = "123456")
    @NotNull
    private String password;
}
