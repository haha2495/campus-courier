package com.example.campus.courier.model.request.student;

import com.example.campus.courier.model.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "学生分页查询请求参数")
public class StudentPageRequest extends BasePageRequest {
    
    @Schema(description = "学号")
    private String studentNo;
    
    @Schema(description = "姓名")
    private String name;
    
    @Schema(description = "院系")
    private String department;
    
    @Schema(description = "专业")
    private String major;
    
    @Schema(description = "年级")
    private String grade;
    
    @Schema(description = "班级")
    private String className;
    
    @Schema(description = "身份类型")
    private Integer identityType;
    
    @Schema(description = "状态")
    private Boolean status;
} 