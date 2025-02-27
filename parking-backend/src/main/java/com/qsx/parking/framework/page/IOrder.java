package com.qsx.parking.framework.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 排序规则对象
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@Schema(description = "排序对象")
public class IOrder {

    @Schema(description = "排序字段名")
    @NotNull
    private String fieldName;

    @Schema(description = "排序方向", example = "asc")
    private String direction;

    public boolean isAsc() {
        return "asc".equalsIgnoreCase(direction);
    }
}
