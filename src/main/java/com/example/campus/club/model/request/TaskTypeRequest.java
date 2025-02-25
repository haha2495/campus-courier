package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "任务类型请求对象")
public class TaskTypeRequest {
    
    @Schema(description = "类型名称")
    @NotBlank(message = "类型名称不能为空")
    @Size(min = 2, max = 20, message = "类型名称长度必须在2-20之间")
    private String name;
    
    @Schema(description = "类型描述")
    @NotBlank(message = "类型描述不能为空")
    @Size(max = 200, message = "类型描述最大长度为200")
    private String description;
} 