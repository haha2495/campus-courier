package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务留言实体类
 */
@Data
@TableName(value = "sys_task_message", autoResultMap = true)
@Schema(description = "任务留言实体")
public class SysTaskMessage {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "留言ID")
    private Integer id;

    @Schema(description = "任务ID")
    private Integer taskId;

    @Schema(description = "留言用户ID")
    private Integer userId;

    @Schema(description = "留言内容")
    private String content;

    @Schema(description = "图片列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 