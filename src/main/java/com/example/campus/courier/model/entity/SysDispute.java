package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "sys_dispute", autoResultMap = true)
@Schema(description = "纠纷实体")
public class SysDispute {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "关联的任务ID")
    private Integer taskId;

    @Schema(description = "发起人ID(学生ID)")
    private Integer initiatorId;

    @Schema(description = "被投诉人ID(学生ID)")
    private Integer respondentId;

    @Schema(description = "纠纷类型 {@link DisputeTypeEnum}")
    private Integer type;

    @Schema(description = "纠纷状态 {@link DisputeStatusEnum}")
    private Integer status;

    @Schema(description = "纠纷标题")
    private String title;

    @Schema(description = "纠纷内容描述")
    private String content;

    @Schema(description = "证据图片列表,JSON数组存储图片URL")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> evidence;

    @Schema(description = "管理员处理备注")
    private String adminRemark;

    @Schema(description = "处理结果:1-支持发起人,2-支持被投诉人,3-协商解决")
    private Integer result;

    @Schema(description = "赔偿金额")
    private BigDecimal compensationAmount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    /**
     * 初始化纠纷实体
     *
     * @param request      创建请求
     * @param initiatorId  发起人ID
     * @param respondentId 被投诉人ID
     * @return 纠纷实体
     */
    public static SysDispute init(DisputeCreateRequest request, Integer initiatorId, Integer respondentId) {
        SysDispute dispute = new SysDispute();
        dispute.setTaskId(request.getTaskId());
        dispute.setType(request.getType());
        dispute.setTitle(request.getTitle());
        dispute.setContent(request.getContent());
        dispute.setEvidence(request.getEvidence());
        dispute.setStatus(0); // 初始状态：待处理
        dispute.setInitiatorId(initiatorId);
        dispute.setRespondentId(respondentId);
        dispute.setCreateTime(LocalDateTime.now());
        return dispute;
    }
} 