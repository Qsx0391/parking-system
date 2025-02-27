CREATE TABLE `sys_operation_log`
(
    `id`         BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `uid`        BIGINT(20) NOT NULL COMMENT '用户ID',
    `duration`   BIGINT(20) NOT NULL COMMENT '操作时长（毫秒）',
    `operation`  VARCHAR(255) NOT NULL COMMENT '操作类型',
    `params`     TEXT COMMENT '请求参数',
    `ip`         VARCHAR(45) COMMENT 'IP地址',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY          `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

CREATE TABLE `sys_parking_lot`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT COMMENT '停车场ID',
    `total_spaces`   INT(11) NOT NULL COMMENT '总车位数',
    `current_spaces` INT(11) NOT NULL COMMENT '当前已用车位数',
    `free_time`      INT(11) NOT NULL COMMENT '免费时长（分钟）',
    `billing_unit`   INT(11) NOT NULL COMMENT '计费单位（分钟）',
    `unit_price`     DECIMAL(10, 2) NOT NULL COMMENT '每单位价格',
    `max_price`      INT(11) NOT NULL COMMENT '最高收费金额',
    `updated_at`     DATETIME NOT NULL COMMENT '更新时间',
    `version`        INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='停车场表';

CREATE TABLE `sys_payment`
(
    `id`         BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
    `uid`        BIGINT(20) NOT NULL COMMENT '用户ID',
    `vid`        VARCHAR(20) NOT NULL COMMENT '车辆ID',
    `vno`        VARCHAR(20) NOT NULL COMMENT '车牌号',
    `amount`     DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    `created_at` DATETIME NOT NULL COMMENT '创建时间',
    `paid_at`    DATETIME DEFAULT NULL COMMENT '支付时间',
    `method`     VARCHAR(50) NOT NULL COMMENT '支付方式',
    `status`     INT(11) NOT NULL COMMENT '支付状态 0：待支付 1：已支付 2：支付失败',
    `version`    INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    PRIMARY KEY (`id`),
    KEY          `idx_vid` (`vid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

CREATE TABLE `sys_user`
(
    `id`       BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `type`     INT(11) NOT NULL COMMENT '用户类型 0：管理员 1：普通用户',
    `version`  INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `sys_vehicle`
(
    `id`         BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `uid`        BIGINT(20) NOT NULL COMMENT '用户ID',
    `vno`        VARCHAR(20) NOT NULL COMMENT '车牌号',
    `entered_at` DATETIME NOT NULL COMMENT '进入时间',
    `paid_at`    DATETIME DEFAULT NULL COMMENT '支付时间',
    `exited_at`  DATETIME DEFAULT NULL COMMENT '离场时间',
    `status`     INT(11) NOT NULL COMMENT '状态 0：未缴费 1：已缴费 2：已离场',
    `fee`        DECIMAL(10, 2) DEFAULT NULL COMMENT '停车费用',
    `version`    INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    PRIMARY KEY (`id`),
    KEY          `idx_vno` (`vno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆信息表';

INSERT INTO `sys_parking_lot` (`id`, `total_spaces`, `current_spaces`, `free_time`, `billing_unit`,
                               `unit_price`, `max_price`, `updated_at`, `version`)
VALUES (1, 100, 100, 30, 60, 5.00, 50, NOW(), 0);
INSERT INTO `sys_user` (`username`, `password`, `type`) VALUES ('admin', MD5('a12345'), 0);