package com.qsx.parking.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 支付记录分页查询响应返回实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "支付记录分页查询响应返回参数")
public class PaymentPageQueryRespDTO {

    /**
     * 支付记录 ID
     */
    @Schema(description = "支付记录ID")
    private String id;

    /**
     * 车牌号
     */
    @Schema(description = "车牌号")
    private String vno;

    /**
     * 支付金额
     */
    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdAt;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String paidAt;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String method;

    /**
     * 支付状态 0:待支付 1:处理中 2:已完成 3:已取消 4:待退款 5:退款中 6:已退款
     */
    @Schema(description = "支付状态 0:待支付 1:处理中 2:已完成 3:已取消 4:待退款 5:退款中 6:已退款")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Integer status;
}
