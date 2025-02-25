package com.example.campus.club.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "院系信息")
public class DepartmentDTO {
    
    @Schema(description = "院系名称")
    private String name;
    
    @Schema(description = "专业列表")
    private List<MajorDTO> majors;
    
    @Data
    @Schema(description = "专业信息")
    public static class MajorDTO {
        
        @Schema(description = "专业名称")
        private String name;
        
        @Schema(description = "年级列表")
        private List<GradeDTO> grades;
    }

    @Data
    @Schema(description = "年级信息")
    public static class GradeDTO {
        
        @Schema(description = "年级名称")
        private String name;
        
        @Schema(description = "班级列表")
        private List<String> classes;
    }
} 