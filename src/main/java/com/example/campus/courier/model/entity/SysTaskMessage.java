package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.campus.courier.model.request.task.TaskMessageAddRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务留言实体类
 */
@Data
@TableName(value = "sys_task_message", autoResultMap = true)
@Schema(description = "任务留言实体")
@Accessors(chain = true)
public class SysTaskMessage {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "任务ID")
    private Integer taskId;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "留言内容")
    private String content;

    @Schema(description = "图片列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 初始化任务留言
     *
     * @param request 留言请求
     * @param userId  用户ID
     * @return 任务留言实体
     */
    public SysTaskMessage init(TaskMessageAddRequest request, Integer userId) {
        this.setTaskId(request.getTaskId())
            .setUserId(userId)
            .setContent(request.getContent())
            // 如果有图片列表，转换为JSON字符串存储
            .setImages(request.getImageUrls())
            .setCreateTime(LocalDateTime.now());
        return this;
    }
} 