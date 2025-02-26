package com.example.campus.courier.model.request.task;

import com.example.campus.courier.model.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "任务查询请求参数")
public class TaskQueryRequest extends BasePageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务状态：0-待接单，1-已接单，2-进行中，3-待确认，4-已完成，5-已取消，6-已过期，7-存在纠纷，不传则查询所有")
    private Integer status;
}