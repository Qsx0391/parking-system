package com.qsx.parking.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 停车信息实体类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_vehicle")
public class VehicleDO {

    /**
     * 车辆 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long uid;

    /**
     * 车牌号
     */
    private String vno;

    /**
     * 入场时间
     */
    private LocalDateTime enteredAt;

    /**
     * 付费时间
     */
    private LocalDateTime paidAt;

    /**
     * 出场时间
     */
    private LocalDateTime exitedAt;

    /**
     * 停车状态
     */
    private Integer status;

    /**
     * 停车费用
     */
    private BigDecimal fee;

    /**
     * 版本号
     */
    @Version
    private Integer version;
}
