package com.example.campus.club.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Schema(description = "跑腿任务VO")
public class CourierTaskVO {
    
    @Schema(description = "任务ID")
    private Long id;
    
    @Schema(description = "任务标题")
    private String title;
    
    @Schema(description = "任务描述")
    private String description;
    
    @Schema(description = "任务类型名称")
    private String typeName;
    
    @Schema(description = "发布者昵称")
    private String publisherName;
    
    @Schema(description = "跑腿者昵称")
    private String courierName;
    
    @Schema(description = "任务状态")
    private Integer status;
    
    @Schema(description = "支付状态")
    private Integer payStatus;
    
    @Schema(description = "报酬金额")
    private BigDecimal reward;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "评价内容")
    private String comment;
    
    @Schema(description = "截止时间")
    private LocalDateTime deadline;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 