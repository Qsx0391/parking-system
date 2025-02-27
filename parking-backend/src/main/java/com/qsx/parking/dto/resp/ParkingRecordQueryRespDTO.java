package com.qsx.parking.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 停车记录查询返回参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "停车记录查询返回参数")
public class ParkingRecordQueryRespDTO {

    /**
     * 车辆 ID
     */
    @Schema(description = "车辆 ID")
    private String id;

    /**
     * 车牌号
     */
    @Schema(description = "车牌号")
    private String vno;

    /**
     * 入场时间
     */
    @Schema(description = "入场时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime enteredAt;

    /**
     * 付费时间
     */
    @Schema(description = "付费时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paidAt;

    /**
     * 出场时间
     */
    @Schema(description = "出场时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime exitedAt;

    /**
     * 停车状态 0:已进入 1:支付中 2:已支付 3:已离场
     */
    @Schema(description = "停车状态 0:已进入 1:支付中 2:已支付 3:已离场")
    private Integer status;
}
