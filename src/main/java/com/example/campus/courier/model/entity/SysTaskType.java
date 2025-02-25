package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务类型实体类
 */
@Data
@TableName("sys_task_types")
@Schema(description = "任务类型实体")
public class SysTaskType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "任务类型ID")
    private Integer id;

    @Schema(description = "类型名称")
    private String typeName;

    @Schema(description = "类型图标")
    private String icon;

    @Schema(description = "类型描述")
    private String description;

    @Schema(description = "优先级(数字越大优先级越高)")
    private Integer priority;

    @Schema(description = "状态(0-禁用,1-启用)")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 初始化任务类型实体
     *
     * @param request 任务类型添加请求
     * @return 当前对象
     */
    public SysTaskType init(TaskTypeAddRequest request) {
        this.typeName = request.getTypeName();
        this.icon = request.getIcon();
        this.description = request.getDescription();
        this.priority = request.getPriority();
        this.status = request.getStatus();
        this.createTime = LocalDateTime.now();
        return this;
    }
} 