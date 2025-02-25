package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "纠纷审批请求")
public class DisputeApprovalRequest {
    
    @NotNull(message = "纠纷ID不能为空")
    @Schema(description = "纠纷ID")
    private Long disputeId;
    
    @NotBlank(message = "处理结果不能为空")
    @Schema(description = "处理结果")
    private String result;
} 