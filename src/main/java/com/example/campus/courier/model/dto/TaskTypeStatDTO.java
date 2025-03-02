package com.example.campus.courier.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "任务类型统计DTO")
public class TaskTypeStatDTO {

    @Schema(description = "任务类型ID")
    private Integer typeId;

    @Schema(description = "类型名称")
    private String typeName;

    @Schema(description = "类型图标")
    private String icon;

    @Schema(description = "任务数量")
    private Integer taskCount;

    @Schema(description = "占比")
    private Double percentage;
} 