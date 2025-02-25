package com.example.campus.club.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "纠纷详情VO")
public class DisputeDetailVO {
    
    @Schema(description = "纠纷ID")
    private Long id;
    
    @Schema(description = "纠纷标题")
    private String title;
    
    @Schema(description = "纠纷描述")
    private String description;
    
    @Schema(description = "纠纷类型名称")
    private String typeName;
    
    @Schema(description = "相关任务标题")
    private String taskTitle;
    
    @Schema(description = "申诉人姓名")
    private String appealerName;
    
    @Schema(description = "被申诉人姓名")
    private String defendantName;
    
    @Schema(description = "处理人姓名")
    private String handlerName;
    
    @Schema(description = "纠纷状态")
    private Integer status;
    
    @Schema(description = "处理结果")
    private String result;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 