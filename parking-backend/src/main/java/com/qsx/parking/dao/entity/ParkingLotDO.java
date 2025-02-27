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
 * 停车场配置实体类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_parking_lot")
public class ParkingLotDO {

    /**
     * 停车场 ID
     */
    private Integer id;

    /**
     * 停车位总数
     */
    private Integer totalSpaces;

    /**
     * 当前停车位数
     */
    private Integer currentSpaces;

    /**
     * 免费停车时长
     */
    private Integer freeTime;

    /**
     * 计费单位，单位分钟
     */
    private Integer billingUnit;

    /**
     * 计费单价
     */
    private BigDecimal unitPrice;

    /**
     * 最高收费
     */
    private Integer maxPrice;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 版本号
     */
    @Version
    private Integer version;
}
