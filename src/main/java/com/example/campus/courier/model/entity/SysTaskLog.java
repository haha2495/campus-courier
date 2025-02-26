package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.campus.courier.model.enums.TaskActionEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务日志实体类
 */
@Data
@TableName("sys_task_log")
@Schema(description = "任务日志实体")
public class SysTaskLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "日志ID")
    private Integer id;

    @Schema(description = "任务ID")
    private Integer taskId;

    @Schema(description = "操作人id")
    private Integer userId;

    @Schema(description = "操作类型：1-发布，2-接单，3-开始执行，4-完成任务，5-确认完成，6-取消任务，7-提交纠纷，8-解决纠纷，9-评价（参见TaskActionEnum枚举类）")
    private Integer action;

    @Schema(description = "备注说明")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "操作时间")
    private LocalDateTime actionTime;

    /**
     * 初始化任务日志
     *
     * @param taskId 任务ID
     * @param action 操作类型
     * @param remark 备注说明
     * @return 当前实体对象
     */
    public SysTaskLog init(Integer taskId, TaskActionEnum action, String remark, Integer userId) {
        this.taskId = taskId;
        this.action = action.getCode();
        this.userId = userId;
        this.remark = remark;
        this.actionTime = LocalDateTime.now();
        return this;
    }
} 