package com.example.campus.courier.model.dto;

import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务数据传输对象，包含任务和任务类型信息
 */
@Data
@Schema(description = "任务数据传输对象")
public class TaskDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务信息")
    private SysTask task;

    @Schema(description = "任务类型信息")
    private SysTaskType taskType;
} 