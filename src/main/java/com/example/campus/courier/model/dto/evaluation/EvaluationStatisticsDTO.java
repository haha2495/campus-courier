package com.example.campus.courier.model.dto.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 评价统计数据传输对象
 */
@Data
@Accessors(chain = true)
@Schema(description = "评价统计数据")
public class EvaluationStatisticsDTO {

    @Schema(description = "总评价数")
    private Long totalCount;

    @Schema(description = "平均评分")
    private BigDecimal averageScore;

    @Schema(description = "好评率")
    private BigDecimal positiveRate;
} 