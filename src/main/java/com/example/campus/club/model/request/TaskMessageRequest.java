package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "任务留言请求")
public class TaskMessageRequest {
    
    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID")
    private Long taskId;
    
    @NotBlank(message = "留言内容不能为空")
    @Schema(description = "留言内容")
    private String content;
} 