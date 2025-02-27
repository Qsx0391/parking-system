package com.qsx.parking.framework.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询基础响应参数
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分页查询基础响应参数")
public class PageQueryRespDTO<T> {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Long pageNumber;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小")
    private Long pageSize;

    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private Long totalPage;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private Long totalSize;

    /**
     * 分页数据
     */
    @Schema(description = "分页数据")
    private List<T> pageData;

    public static <T> PageQueryRespDTO<T> of(IPage<T> page) {
        return PageQueryRespDTO.<T>builder()
                .pageNumber(page.getCurrent())
                .pageSize(page.getSize())
                .totalPage(page.getPages())
                .totalSize(page.getTotal())
                .pageData(page.getRecords())
                .build();
    }
}
