package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_dispute")
@Schema(description = "纠纷实体")
public class SysDispute {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关联的任务ID")
    private Long taskId;

    @Schema(description = "发起人ID(学生ID)")
    private Long initiatorId;

    @Schema(description = "被投诉人ID(学生ID)")
    private Long respondentId;

    @Schema(description = "纠纷类型 {@link DisputeTypeEnum}")
    private Integer type;

    @Schema(description = "纠纷状态 {@link DisputeStatusEnum}")
    private Integer status;

    @Schema(description = "纠纷标题")
    private String title;

    @Schema(description = "纠纷内容描述")
    private String content;

    @Schema(description = "证据图片列表,JSON数组存储图片URL")
    private String evidence;

    @Schema(description = "管理员处理备注")
    private String adminRemark;

    @Schema(description = "处理结果:1-支持发起人,2-支持被投诉人,3-协商解决")
    private Integer result;

    @Schema(description = "赔偿金额")
    private BigDecimal compensationAmount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 