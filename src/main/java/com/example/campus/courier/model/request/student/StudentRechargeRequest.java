package com.example.campus.courier.model.request.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "学生充值请求")
public class StudentRechargeRequest {

    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "充值金额格式不正确")
    @Schema(description = "充值金额")
    private BigDecimal amount;
} 