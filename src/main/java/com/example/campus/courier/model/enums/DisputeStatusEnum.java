package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 纠纷状态枚举
 */
@Getter
@AllArgsConstructor
public enum DisputeStatusEnum {
    
    PENDING(0, "待处理"),
    PROCESSING(1, "处理中"),
    FINISHED(2, "已结束"),
    PUBLISHED(3, "已发布纠纷大厅");
    
    private final Integer code;
    private final String desc;
    
    public static DisputeStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DisputeStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}