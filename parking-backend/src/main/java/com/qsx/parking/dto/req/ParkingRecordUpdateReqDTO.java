package com.qsx.parking.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 停车记录修改请求参数实体
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "停车记录修改请求参数")
public class ParkingRecordUpdateReqDTO {

    /**
     * 车辆 ID
     */
    @Schema(description = "车辆 ID", example = "1895035942162595841")
    @NotNull
    private String id;

    /**
     * 车牌号
     */
    @Schema(description = "车牌号", example = "粤B88888")
    private String vno;

    /**
     * 入场时间
     */
    @Schema(description = "入场时间", example = "2025-02-26 14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime enteredAt;
}
