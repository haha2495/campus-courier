package com.example.campus.courier.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "任务趋势统计DTO")
public class TaskTrendDTO {

    @Schema(description = "日期列表")
    private List<String> dateList;

    @Schema(description = "已完成任务数列表")
    private List<Integer> completedList;

    @Schema(description = "进行中任务数列表")
    private List<Integer> processingList;

    @Schema(description = "待接单任务数列表")
    private List<Integer> pendingList;
} 