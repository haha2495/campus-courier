package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_dispute_support")
@Schema(description = "纠纷支持实体")
public class SysDisputeSupport {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;
    
    @Schema(description = "关联的纠纷ID")
    private Integer disputeId;
    
    @Schema(description = "支持者ID(学生ID)")
    private Integer studentId;
    
    @Schema(description = "支持类型:1-支持发起人,2-支持被投诉人")
    private Integer supportType;
    
    @Schema(description = "支持原因")
    private String reason;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 