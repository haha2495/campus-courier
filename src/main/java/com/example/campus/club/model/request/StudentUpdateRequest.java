package com.example.campus.club.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生更新请求")
public class StudentUpdateRequest {
    
    @Schema(description = "学生ID")
    private Integer id;
    
    @Schema(description = "姓名")
    private String name;
    
    @Schema(description = "性别(1:男,2:女)")
    private Integer gender;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "院系")
    private String department;
    
    @Schema(description = "专业")
    private String major;
    
    @Schema(description = "年级")
    private String grade;
    
    @Schema(description = "班级")
    private String className;
    
    @Schema(description = "入学年份")
    private Integer enrollmentYear;
    
    @Schema(description = "状态(true:正常,false:禁用)")
    private Boolean status;
} 