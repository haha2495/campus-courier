package com.example.campus.courier.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "管理员个人信息更新请求")
public class AdminProfileUpdateRequest {

    @Schema(description = "昵称")
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "新密码")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    private String password;
} 