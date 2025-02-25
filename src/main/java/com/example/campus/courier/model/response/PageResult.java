package com.example.campus.courier.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Schema(description = "分页结果")
public class PageResult<T> {
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "数据列表")
    private List<T> records;
    
    /**
     * 构建分页结果
     *
     * @param total 总记录数
     * @param records 数据列表
     * @return 分页结果
     */
    public static <T> PageResult<T> build(Long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setRecords(records);
        return result;
    }

    /**
     * 构建空的分页结果
     *
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty() {
        return build(0L, Collections.emptyList());
    }
} 