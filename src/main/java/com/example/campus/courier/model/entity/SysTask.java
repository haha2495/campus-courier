package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务实体类
 */
@Data
@TableName(value = "sys_task",autoResultMap = true)
@Schema(description = "任务实体")
public class SysTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "任务标题")
    private String title;

    @Schema(description = "任务详细描述")
    private String description;

    @Schema(description = "发布者用户ID")
    private Long publisherId;

    @Schema(description = "接单者用户ID")
    private Long runnerId;

    @Schema(description = "任务类型ID")
    private Integer taskTypeId;

    @Schema(description = "任务状态：0-待接单，1-已接单，2-进行中，3-待确认，4-已完成，5-已取消，6-已过期，7-存在纠纷（参见TaskStatusEnum枚举类）")
    private Integer status;

    @Schema(description = "任务报酬")
    private BigDecimal reward;

    @Schema(description = "是否加急：0-否，1-是")
    private Boolean urgent;

    @Schema(description = "加急费")
    private BigDecimal urgentReward;

    @Schema(description = "取件地址")
    private String pickupAddress;

    @Schema(description = "送达地址")
    private String deliveryAddress;

    @Schema(description = "预估重量(kg)")
    private BigDecimal estimatedWeight;

    @Schema(description = "预估大小（如小、中、大）")
    private String estimatedSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "预期完成时间")
    private LocalDateTime expectedFinishTime;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "图片(JSON格式)")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    @Schema(description = "备注说明")
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 