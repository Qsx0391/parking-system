package com.qsx.parking.framework.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 分页查询基础请求参数
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@Schema(description = "分页查询基础请求参数")
public class BasePageQueryReqDTO<T> {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @NotNull
    private Integer pageNumber;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10")
    @NotNull
    private Integer pageSize;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private List<IOrder> orders;

    /**
     * 获取分页对象
     */
    public IPage<T> getPage() {
        return new Page<>(pageNumber, pageSize);
    }

    public boolean checkOrders(List<String> validFieldNames) {
        if (orders == null) {
            return true;
        }
        for (IOrder order : orders) {
            if (!validFieldNames.contains(order.getFieldName())) {
                return false;
            }
        }
        return true;
    }
}
