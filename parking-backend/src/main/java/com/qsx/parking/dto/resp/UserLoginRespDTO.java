package com.qsx.parking.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回参数实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户登录返回参数")
public class UserLoginRespDTO {

    /**
     * 用户 ID
     */
    @Schema(description = "用户类型 0:管理员 1:停车用户")
    private Integer type;

    /**
     * token
     */
    @Schema(description = "token")
    private String token;
}
