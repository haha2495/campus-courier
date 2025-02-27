package com.example.campus.courier.model.request.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 添加评价请求
 */
@Data
@Schema(description = "添加评价请求")
public class EvaluationAddRequest {

    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID")
    private Integer taskId;

    @NotNull(message = "评分不能为空")
    @DecimalMin(value = "1.0", message = "评分最低为1分")
    @DecimalMax(value = "5.0", message = "评分最高为5分")
    @Digits(integer = 1, fraction = 1, message = "评分只支持一位小数")
    @Schema(description = "评分（1-5分，支持半分）")
    private BigDecimal score;

    @Size(max = 500, message = "评价内容不能超过500字")
    @Schema(description = "评价内容")
    private String content;

    @Size(max = 5, message = "最多选择5个标签")
    @Schema(description = "评价标签")
    private List<String> tags;

    @Schema(description = "是否匿名评价")
    private Boolean anonymous = false;
} 