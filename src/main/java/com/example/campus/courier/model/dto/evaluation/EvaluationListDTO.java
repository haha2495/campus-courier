package com.example.campus.courier.model.dto.evaluation;

import com.example.campus.courier.model.entity.SysTaskEvaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 评价列表数据传输对象
 */
@Data
@Accessors(chain = true)
@Schema(description = "评价列表数据")
public class EvaluationListDTO {

    @Schema(description = "评价统计信息")
    private EvaluationStatisticsDTO statistics;

    @Schema(description = "评价详情列表")
    private List<SysTaskEvaluation> evaluations;
} 