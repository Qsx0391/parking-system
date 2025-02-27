package com.qsx.parking.framework.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsx.parking.framework.exception.ClientException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 带排序功能的 LambdaQueryWrapper
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public class OrderLambdaPageQueryWrapper<T> extends LambdaQueryWrapper<T> {

    /**
     * 根据排序规则表构建排序语句
     * @param orders      排序规则表
     * @return this
     */
    public OrderLambdaPageQueryWrapper<T> orderBy(List<IOrder> orders, Class<?> entityClass) {
        if (CollUtil.isEmpty(orders)) {
            return this;
        }

        StringBuilder sqlBuilder = new StringBuilder("ORDER BY");
        orders.forEach(order -> {
            String column = getColumnName(order.getFieldName(), entityClass);
            sqlBuilder.append(String.format(" %s %s", column, order.isAsc() ? "ASC" : "DESC"));
        });
        this.last(sqlBuilder.toString());
        return this;
    }

    private String getColumnName(String fieldName, Class<?> entityClass) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            TableField tableField = field.getAnnotation(TableField.class);
            
            // 如果有@TableField注解且指定了value，使用注解指定的列名
            if (tableField != null && !tableField.value().isEmpty()) {
                return tableField.value();
            }
            
            // 如果字段标记为不存在于数据库，抛出异常
            if (tableField != null && !tableField.exist()) {
                throw new ClientException(String.format("排序字段 %s 不存在", fieldName));
            }
            
            // 否则使用驼峰转下划线
            return StrUtil.toUnderlineCase(fieldName);
        } catch (NoSuchFieldException e) {
            throw new ClientException(String.format("排序字段 %s 不存在", fieldName));
        }
    }
}
