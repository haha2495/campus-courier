package com.example.campus.courier.model.request.dispute;

import com.example.campus.courier.model.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 纠纷分页查询请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "纠纷分页查询请求参数")
public class DisputePageRequest extends BasePageRequest {

    @Schema(description = "纠纷状态：0-待处理，1-处理中，2-已完成", example = "0")
    private Integer status;
} 