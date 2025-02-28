package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 纠纷类型枚举
 */
@Getter
@AllArgsConstructor
public enum DisputeTypeEnum {
    
    DELIVERY_MISMATCH(1, "送达与实际不符"),
    BAD_ATTITUDE(2, "态度恶劣"),
    GOODS_DAMAGED(3, "商品损坏"),
    DELIVERY_DELAY(4, "送达延迟"),
    WRONG_DELIVERY(5, "送错商品"),
    LOST_GOODS(6, "商品丢失"),
    PRICE_DISPUTE(7, "价格争议"),
    SAFETY_ISSUE(8, "安全问题"),
    UNPROFESSIONAL(9, "不专业行为"),
    OTHER(10, "其他");
    
    private final Integer code;
    private final String desc;
    
    public static DisputeTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DisputeTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 