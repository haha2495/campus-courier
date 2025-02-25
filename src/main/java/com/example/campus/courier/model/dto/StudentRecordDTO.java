package com.example.campus.courier.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "学生记录DTO")
public class StudentRecordDTO {
    
    @Schema(description = "记录类型描述")
    private String type;
    
    @Schema(description = "记录内容")
    private String content;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
} 