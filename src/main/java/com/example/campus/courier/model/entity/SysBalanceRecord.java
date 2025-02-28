package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("sys_balance_record")
@Schema(description = "余额变动记录")
public class SysBalanceRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "学生ID")
    private Integer studentId;

    @Schema(description = "变动金额")
    private BigDecimal amount;

    @Schema(description = "变动类型：1-充值，2-提现，3-收入，4-支出")
    private Integer type;

    @Schema(description = "变动前余额")
    private BigDecimal beforeBalance;

    @Schema(description = "变动后余额")
    private BigDecimal afterBalance;

    @Schema(description = "变动说明")
    private String description;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
} 