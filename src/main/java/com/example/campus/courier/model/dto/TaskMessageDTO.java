package com.example.campus.courier.model.dto;

import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.entity.SysTaskMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务留言DTO，包含留言信息和发送者信息
 */
@Data
@Schema(description = "任务留言DTO")
public class TaskMessageDTO {

    @Schema(description = "留言信息")
    private SysTaskMessage message;

    @Schema(description = "发送者信息")
    private SysStudent student;

    /**
     * 构建任务留言DTO
     *
     * @param message 留言信息
     * @param student 发送者信息
     * @return 任务留言DTO
     */
    public static TaskMessageDTO build(SysTaskMessage message, SysStudent student) {
        TaskMessageDTO dto = new TaskMessageDTO();
        dto.message = message;
        dto.student = student;
        return dto;
    }
} 