package com.qsx.parking.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_operation_log")
public class OperationLogDO {

    /**
     * 日志 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long uid;

    /**
     * 操作耗时
     */
    private Long duration;

    /**
     * 操作
     */
    private String operation;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求 IP 地址
     */
    private String ip;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

}
