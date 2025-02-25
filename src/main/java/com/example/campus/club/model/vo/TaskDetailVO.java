package com.example.campus.club.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Schema(description = "任务详情VO")
public class TaskDetailVO {
    
    @Schema(description = "任务ID")
    private Long id;
    
    @Schema(description = "任务标题")
    private String title;
    
    @Schema(description = "任务描述")
    private String description;
    
    @Schema(description = "任务类型名称")
    private String typeName;
    
    @Schema(description = "发布者姓名")
    private String publisherName;
    
    @Schema(description = "接单者姓名")
    private String receiverName;
    
    @Schema(description = "任务状态(0:待接单 1:进行中 2:已完成 3:已取消)")
    private Integer status;
    
    @Schema(description = "报酬金额")
    private BigDecimal reward;
    
    @Schema(description = "截止时间")
    private LocalDateTime deadline;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 