package com.example.campus.courier.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "首页统计数据DTO")
public class DashboardStatDTO {

    @Schema(description = "总任务数")
    private Integer totalTaskCount;

    @Schema(description = "总任务数同比增长")
    private Double totalTaskGrowth;

    @Schema(description = "活跃用户数")
    private Integer activeUserCount;

    @Schema(description = "活跃用户同比增长")
    private Double activeUserGrowth;

    @Schema(description = "总收入")
    private Double totalIncome;

    @Schema(description = "总收入同比增长")
    private Double totalIncomeGrowth;

    @Schema(description = "任务完成率")
    private Double taskCompletionRate;

    @Schema(description = "完成率同比变化")
    private Double completionRateChange;
} 