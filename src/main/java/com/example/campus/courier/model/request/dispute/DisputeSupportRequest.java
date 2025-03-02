package com.example.campus.courier.model.request.dispute;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "纠纷支持请求")
public class DisputeSupportRequest {
    
    @NotNull(message = "纠纷ID不能为空")
    @Schema(description = "纠纷ID")
    private Integer disputeId;
    
    @NotNull(message = "支持类型不能为空")
    @Schema(description = "支持类型:1-支持发起人,2-支持被投诉人")
    private Integer supportType;
    
    @Schema(description = "支持原因")
    private String remark;
} 