package com.qsx.parking.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付信息响应数据返回实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "支付信息响应数据返回")
public class PaymentInfoRespDTO {

    /**
     * 车辆 ID
     */
    @Schema(description = "车辆ID")
    private String vid;

    /**
     * 支付 ID
     */
    @Schema(description = "支付ID")
    private String pid;

    /**
     * 停车费用
     */
    @Schema(description = "停车费用")
    private BigDecimal fee;

    /**
     * 入场时间
     */
    @Schema(description = "入场时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime enteredAt;

    /**
     * 出场时间
     */
    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paymentCreateAt;
}
