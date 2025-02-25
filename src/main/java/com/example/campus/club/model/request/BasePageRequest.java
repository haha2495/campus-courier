package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "基础分页请求参数")
public class BasePageRequest {
    
    @Schema(description = "页码", defaultValue = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页条数", defaultValue = "10")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能大于100")
    private Integer pageSize = 10;
} 