package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task_message")
@Schema(description = "任务留言实体")
public class TaskMessage {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "任务ID")
    private Long taskId;
    
    @Schema(description = "发送者ID")
    private Long senderId;
    
    @Schema(description = "消息内容")
    private String content;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 