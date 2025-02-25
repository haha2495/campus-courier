package com.example.campus.courier.model.request.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "学生个人信息更新请求")
public class StudentProfileUpdateRequest {

    @Schema(description = "姓名")
    @Size(max = 20, message = "姓名长度不能超过20个字符")
    private String name;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "邮箱")
    @Email(message = "请输入正确的邮箱格式")
    private String email;

    @Schema(description = "新密码")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    private String password;
} 