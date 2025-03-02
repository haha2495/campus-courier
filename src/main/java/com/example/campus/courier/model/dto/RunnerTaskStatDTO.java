package com.example.campus.courier.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 跑腿者任务统计DTO
 */
@Data
@Schema(description = "跑腿者任务统计DTO")
public class RunnerTaskStatDTO {

    @Schema(description = "已完成任务列表")
    private List<TaskDTO> taskList;

    @Schema(description = "总完成数量")
    private Integer totalCompleted;

    @Schema(description = "总赚取金额")
    private BigDecimal totalEarnings;

    @Schema(description = "本月完成数量")
    private Integer monthlyCompleted;

    @Schema(description = "本月赚取金额")
    private BigDecimal monthlyEarnings;
}