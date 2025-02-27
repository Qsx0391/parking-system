package com.qsx.parking.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * 停车场配置信息更新请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Getter
public class ParkingLotConfigUpdateReqDTO {

    /**
     * 停车位总数
     */
    @Schema(description = "停车位总数", example = "100")
    private Integer totalSpaces;

    /**
     * 免费停车时长
     */
    @Schema(description = "免费停车时长", example = "30")
    private Integer freeTime;

    /**
     * 计费单位，单位分钟
     */
    @Schema(description = "计费单位，单位分钟", example = "30")
    private Integer billingUnit;

    /**
     * 计费单价
     */
    @Schema(description = "计费单价", example = "5.00")
    private BigDecimal unitPrice;

    /**
     * 最高收费
     */
    @Schema(description = "最高收费", example = "50")
    private Integer maxPrice;
}
