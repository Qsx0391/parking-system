package com.qsx.parking.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日志记录分页查询返回参数
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "日志记录分页查询返回参数")
public class LogRecordQueryRespDTO {

    /**
     * 日志 ID
     */
    @Schema(description = "日志 ID")
    private String id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 操作耗时
     */
    @Schema(description = "操作耗时")
    private Long duration;

    /**
     * 操作
     */
    @Schema(description = "操作")
    private String operation;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String params;

    /**
     * 请求 IP 地址
     */
    @Schema(description = "请求 IP 地址")
    private String ip;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}
