package com.example.campus.courier.model.request.dispute;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * 纠纷处理请求
 */
@Data
@Schema(description = "纠纷处理请求")
public class DisputeHandleRequest {

    @NotNull(message = "纠纷ID不能为空")
    @Schema(description = "纠纷ID")
    private Integer disputeId;

    @NotNull(message = "处理结果不能为空")
    @Schema(description = "处理结果")
    private Integer handleResult;

    @Size(max = 500, message = "处理说明不能超过500字")
    @Schema(description = "处理说明")
    private String handleRemark;

    @Schema(description = "展示时长（小时）")
    @Min(value = 1, message = "展示时长不能小于1小时")
    @Max(value = 168, message = "展示时长不能超过168小时（7天）")
    private Integer displayHours;
} 