package com.example.campus.courier.model.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "管理员修改请求参数")
public class AdminUpdateRequest {

    @Schema(description = "管理员ID", required = true)
    @NotNull(message = "管理员ID不能为空")
    private Integer id;

    @Schema(description = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description = "状态：true-正常，false-禁用", required = true)
    @NotNull(message = "状态不能为空")
    private Boolean status;
} 