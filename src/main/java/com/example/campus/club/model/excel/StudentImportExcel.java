package com.example.campus.club.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生导入Excel对象")
public class StudentImportExcel {

    @ExcelProperty(index = 0)
    @Schema(description = "学号")
    private String studentNo;

    @ExcelProperty(index = 1)
    @Schema(description = "姓名")
    private String name;

    @ExcelProperty(index = 2)
    @Schema(description = "性别")
    private String gender;

    @ExcelProperty(index = 3)
    @Schema(description = "邮箱")
    private String email;

    @ExcelProperty(index = 4)
    @Schema(description = "院系")
    private String department;

    @ExcelProperty(index = 5)
    @Schema(description = "专业")
    private String major;

    @ExcelProperty(index = 6)
    @Schema(description = "年级")
    private String grade;

    @ExcelProperty(index = 7)
    @Schema(description = "班级")
    private String className;

    @ExcelProperty(index = 8)
    @Schema(description = "状态")
    private String status;
} 