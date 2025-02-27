package com.example.campus.courier.model.request.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "任务留言添加请求")
public class TaskMessageAddRequest {

    @Schema(description = "任务ID")
    @NotNull(message = "任务ID不能为空")
    private Integer taskId;

    @Schema(description = "留言内容")
    @NotBlank(message = "留言内容不能为空")
    private String content;
    
    @Schema(description = "图片URL列表")
    private List<String> imageUrls = new ArrayList<>();
} 