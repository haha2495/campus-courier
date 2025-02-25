package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "学生登录请求参数")
public class StudentLoginRequest {

    @Schema(description = "学号", required = true)
    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

}