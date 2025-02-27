package com.example.campus.courier.model.dto;

import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskMessage;
import com.example.campus.courier.model.entity.SysTaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 任务详情DTO
 */
@Data
@Schema(description = "任务详情DTO")
public class TaskDetailDTO {

    @Schema(description = "任务信息")
    private SysTask task;

    @Schema(description = "任务类型信息")
    private SysTaskType taskType;

    @Schema(description = "任务留言列表")
    private List<SysTaskMessage> messages;

    /**
     * 构建任务详情DTO
     *
     * @param task 任务信息
     * @param taskType 任务类型信息
     * @param messages 任务留言列表
     * @return 任务详情DTO
     */
    public static TaskDetailDTO build(SysTask task, SysTaskType taskType, List<SysTaskMessage> messages) {
        TaskDetailDTO dto = new TaskDetailDTO();
        dto.task = task;
        dto.taskType = taskType;
        dto.messages = messages;
        return dto;
    }
} 