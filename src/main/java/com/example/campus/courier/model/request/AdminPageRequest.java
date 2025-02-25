package com.example.campus.courier.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员分页查询请求参数")
public class AdminPageRequest extends BasePageRequest {
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "昵称")
    private String nickname;
    
    @Schema(description = "状态：1-正常，0-禁用")
    private Boolean status;
} 