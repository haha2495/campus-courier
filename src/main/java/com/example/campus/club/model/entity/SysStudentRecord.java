package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.campus.club.model.enums.StudentRecordType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("sys_student_record")
@Schema(description = "学生记录实体")
public class SysStudentRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Integer id;

    @Schema(description = "学生ID")
    private Integer studentId;

    @Schema(description = "记录类型")
    private Integer type;

    @Schema(description = "详细内容")
    private String content;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 创建记录
     */
    public static SysStudentRecord build(Integer studentId, StudentRecordType type, String content) {
        return new SysStudentRecord()
                .setStudentId(studentId)
                .setType(type.getCode())
                .setContent(content);
    }
} 