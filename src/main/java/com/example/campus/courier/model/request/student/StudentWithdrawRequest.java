package com.example.campus.courier.model.request.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "学生提现请求")
public class StudentWithdrawRequest {

    @Schema(description = "提现金额")
    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "1.00", message = "提现金额必须大于1元")
    @Digits(integer = 8, fraction = 2, message = "提现金额格式不正确，最多8位整数，2位小数")
    private BigDecimal amount;
} 