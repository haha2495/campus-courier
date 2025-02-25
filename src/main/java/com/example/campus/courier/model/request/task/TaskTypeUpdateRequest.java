package com.example.campus.courier.model.request.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 任务类型更新请求对象
 */
@Data
@Schema(description = "任务类型更新请求")
public class TaskTypeUpdateRequest {

    @NotNull(message = "任务类型ID不能为空")
    @Schema(description = "任务类型ID", required = true)
    private Integer id;

    @NotBlank(message = "类型名称不能为空")
    @Size(min = 2, max = 50, message = "类型名称长度应在2-50个字符之间")
    @Schema(description = "类型名称", required = true)
    private String typeName;

    @NotBlank(message = "类型图标不能为空")
    @Schema(description = "类型图标URL", required = true)
    private String icon;

    @NotBlank(message = "类型描述不能为空")
    @Size(max = 200, message = "类型描述长度不能超过200个字符")
    @Schema(description = "类型描述", required = true)
    private String description;

    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级最小为1")
    @Max(value = 100, message = "优先级最大为100")
    @Schema(description = "优先级(数字越大优先级越高)", required = true)
    private Integer priority;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态(0-禁用,1-启用)", required = true)
    private Integer status;
} 