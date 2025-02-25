package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task_type")
@Schema(description = "任务类型实体")
public class TaskType {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "类型名称")
    private String name;
    
    @Schema(description = "类型描述")
    private String description;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 