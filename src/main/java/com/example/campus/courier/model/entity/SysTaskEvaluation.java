package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务评价实体类
 */
@Data
@TableName(value = "sys_task_evaluation", autoResultMap = true)
@Schema(description = "任务评价实体")
@Accessors(chain = true)
public class SysTaskEvaluation {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "任务ID")
    private Integer taskId;

    @Schema(description = "发布者ID（评价人）")
    private Integer publisherId;

    @Schema(description = "跑腿者ID（被评价人）")
    private Integer runnerId;

    @Schema(description = "评分（1-5分，支持半分）")
    private BigDecimal score;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价标签")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    @Schema(description = "是否匿名评价（0-否，1-是）")
    private Boolean anonymous;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 