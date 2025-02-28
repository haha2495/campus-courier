package com.example.campus.courier.model.request.dispute;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建纠纷请求")
public class DisputeCreateRequest {
    
    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID", required = true)
    private Integer taskId;
    
    @NotNull(message = "纠纷类型不能为空")
    @Schema(description = "纠纷类型", required = true)
    private Integer type;
    
    @NotBlank(message = "纠纷标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50个字符")
    @Schema(description = "纠纷标题", required = true)
    private String title;
    
    @NotBlank(message = "纠纷内容不能为空")
    @Size(max = 500, message = "内容长度不能超过500个字符")
    @Schema(description = "纠纷内容", required = true)
    private String content;
    
    @Schema(description = "证据图片列表")
    private List<String> evidence;
} 