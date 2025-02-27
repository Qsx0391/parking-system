package com.qsx.parking.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.framework.page.BasePageQueryReqDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 分页查询缴费记录请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Getter
@Schema(description = "分页查询缴费记录请求参数")
public class PaymentPageQueryReqDTO extends BasePageQueryReqDTO<PaymentDO> {

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
     * 支付状态 0:待支付 1:处理中 2:已完成 3:已取消 4:待退款 5:退款中 6:已退款
     */
    @Schema(description = "支付状态 0:待支付 1:处理中 2:已完成 3:已取消 4:待退款 5:退款中 6:已退款", example = "1")
    private Integer status;

}
