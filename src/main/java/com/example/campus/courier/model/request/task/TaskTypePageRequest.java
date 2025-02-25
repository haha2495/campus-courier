package com.example.campus.courier.model.request.task;

import com.example.campus.courier.model.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务类型分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务类型分页查询请求")
public class TaskTypePageRequest extends BasePageRequest {

    @Schema(description = "状态(0-禁用,1-启用), 不传则查询全部")
    private Integer status;
} 