package com.qsx.parking.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 停车场配置信息响应参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "停车场配置信息响应参数")
public class ParkingLotConfigRespDTO {

    /**
     * 停车位总数
     */
    @Schema(description = "停车位总数")
    private Integer totalSpaces;

    /**
     * 当前停车位数
     */
    @Schema(description = "当前停车位数")
    private Integer currentSpaces;

    /**
     * 免费停车时长
     */
    @Schema(description = "免费停车时长")
    private Integer freeTime;

    /**
     * 计费单位，单位分钟
     */
    @Schema(description = "计费单位，单位分钟")
    private Integer billingUnit;

    /**
     * 计费单价
     */
    @Schema(description = "计费单价")
    private BigDecimal unitPrice;

    /**
     * 最高收费
     */
    @Schema(description = "最高收费")
    private Integer maxPrice;
}
