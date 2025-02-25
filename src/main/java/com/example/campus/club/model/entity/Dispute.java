package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dispute")
@Schema(description = "纠纷实体")
public class Dispute {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "纠纷标题")
    private String title;
    
    @Schema(description = "纠纷描述")
    private String description;
    
    @Schema(description = "纠纷类型ID")
    private Long typeId;
    
    @Schema(description = "相关任务ID")
    private Long taskId;
    
    @Schema(description = "申诉人ID")
    private Long appealerId;
    
    @Schema(description = "被申诉人ID")
    private Long defendantId;
    
    @Schema(description = "处理人ID")
    private Long handlerId;
    
    @Schema(description = "纠纷状态(0:待处理 1:处理中 2:已完成 3:已转发)")
    private Integer status;
    
    @Schema(description = "处理结果")
    private String result;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 