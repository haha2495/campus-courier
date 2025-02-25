package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("task")
@Schema(description = "任务实体")
public class Task {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "任务标题")
    private String title;
    
    @Schema(description = "任务描述")
    private String description;
    
    @Schema(description = "任务类型ID")
    private Long typeId;
    
    @Schema(description = "发布者ID")
    private Long publisherId;
    
    @Schema(description = "跑腿者ID")
    private Long courierId;
    
    @Schema(description = "任务状态(0:待接单 1:进行中 2:已完成 3:已取消)")
    private Integer status;
    
    @Schema(description = "支付状态(0:未支付 1:已支付)")
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
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 