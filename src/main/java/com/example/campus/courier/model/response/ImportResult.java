package com.example.campus.courier.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "导入结果")
public class ImportResult {
    
    @Schema(description = "是否全部成功")
    private boolean success;
    
    @Schema(description = "总记录数")
    private int total;
    
    @Schema(description = "成功记录数")
    private int successCount;
    
    @Schema(description = "失败记录数")
    private int failCount;
    
    @Schema(description = "错误信息列表")
    private List<ErrorDetail> errors = new ArrayList<>();
    
    @Data
    @Schema(description = "错误详情")
    public static class ErrorDetail {
        @Schema(description = "行号")
        private int rowIndex;
        
        @Schema(description = "学号")
        private String studentNo;
        
        @Schema(description = "错误信息")
        private String message;
        
        public static ErrorDetail of(int rowIndex, String studentNo, String message) {
            ErrorDetail detail = new ErrorDetail();
            detail.setRowIndex(rowIndex);
            detail.setStudentNo(studentNo);
            detail.setMessage(message);
            return detail;
        }
    }
    
    public void addError(int rowIndex, String studentNo, String message) {
        this.errors.add(ErrorDetail.of(rowIndex, studentNo, message));
        this.failCount++;
    }
    
    public void incrementSuccess() {
        this.successCount++;
    }

    public void incrementTotal() {
        this.total++;
    }
    
    public static ImportResult build(int total) {
        ImportResult result = new ImportResult();
        result.setTotal(total);
        return result;
    }
    
    public void complete() {
        this.success = failCount == 0;
    }
} 