package com.example.campus.courier.model.dto;

import com.example.campus.courier.model.entity.SysDispute;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.entity.SysTaskLog;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
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

    @Schema(description = "发布者信息")
    private SysStudent publisher;

    @Schema(description = "跑腿者信息")
    private SysStudent runner;

    @Schema(description = "纠纷信息")
    private SysDispute dispute;

    @Schema(description = "任务留言列表")
    private List<TaskMessageDTO> messages;

    @Schema(description = "任务日志列表(按时间倒序)")
    private List<SysTaskLog> taskLogs;

    @Schema(description = "任务评价信息")
    private SysTaskEvaluation evaluation;

    /**
     * 构建任务详情DTO
     *
     * @param task 任务信息
     * @param taskType 任务类型信息
     * @param publisher 发布者信息
     * @param runner 跑腿者信息
     * @param dispute 纠纷信息
     * @param messages 任务留言列表
     * @param taskLogs 任务日志列表
     * @param evaluation 任务评价信息
     * @return 任务详情DTO
     */
    public static TaskDetailDTO build(
            SysTask task, 
            SysTaskType taskType,
            SysStudent publisher,
            SysStudent runner,
            SysDispute dispute,
            List<TaskMessageDTO> messages, 
            List<SysTaskLog> taskLogs, 
            SysTaskEvaluation evaluation) {
        TaskDetailDTO dto = new TaskDetailDTO();
        dto.task = task;
        dto.taskType = taskType;
        dto.publisher = publisher;
        dto.runner = runner;
        dto.dispute = dispute;
        dto.messages = messages;
        dto.taskLogs = taskLogs;
        dto.evaluation = evaluation;
        return dto;
    }
} 