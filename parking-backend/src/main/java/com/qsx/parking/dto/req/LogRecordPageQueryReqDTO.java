package com.qsx.parking.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qsx.parking.dao.entity.OperationLogDO;
import com.qsx.parking.framework.page.BasePageQueryReqDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 操作日志分页查询请求参数实体类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Getter
@Schema(description = "操作日志分页查询请求参数")
public class LogRecordPageQueryReqDTO extends BasePageQueryReqDTO<OperationLogDO> {

    /**
     * 操作人用户名
     */
    @Schema(description = "操作人用户名", example = "admin")
    private String username;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型", example = "vehicle/entry")
    private String operation;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2021-07-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startedAt;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2021-07-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endedAt;

}
