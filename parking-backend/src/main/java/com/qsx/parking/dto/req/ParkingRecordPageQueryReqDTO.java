package com.qsx.parking.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qsx.parking.dao.entity.VehicleDO;
import com.qsx.parking.framework.page.BasePageQueryReqDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 分页查询停车记录请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Getter
@Schema(description = "分页查询停车记录请求参数")
public class ParkingRecordPageQueryReqDTO extends BasePageQueryReqDTO<VehicleDO> {

    /**
     * 车牌号
     */
    @Schema(description = "车牌号", example = "粤B12345")
    private String vno;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2025-01-01T00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startedAt;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2025-01-01T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endedAt;

    /**
     * 停车状态 0:已进入 1:支付中 2:已支付 3:已离场
     */
    @Schema(description = "停车状态", example = "1")
    private Integer status;

}
