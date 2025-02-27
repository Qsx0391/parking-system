package com.qsx.parking.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款记录实体类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_payment")
public class PaymentDO {

    /**
     * 付款记录 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long uid;

    /**
     * 车辆 ID
     */
    private Long vid;

    /**
     * 车牌号
     */
    private String vno;

    /**
     * 付款金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 付款时间
     */
    private LocalDateTime paidAt;

    /**
     * 付款方式
     */
    private String method;

    /**
     * 付款状态
     */
    private Integer status;

    /**
     * 版本号
     */
    @Version
    private Integer version;
}
