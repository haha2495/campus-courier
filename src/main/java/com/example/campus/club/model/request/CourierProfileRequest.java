package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "跑腿者个人信息请求")
public class CourierProfileRequest {
    
    @Schema(description = "头像")
    private String avatar;
    
    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称最大长度为20")
    @Schema(description = "昵称")
    private String nickname;
    
    @NotBlank(message = "联系方式不能为空")
    @Schema(description = "联系方式")
    private String contact;
    
    @Size(max = 200, message = "个人简介最大长度为200")
    @Schema(description = "个人简介")
    private String introduction;
} 