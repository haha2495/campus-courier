package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 纠纷处理结果枚举
 */
@Getter
@AllArgsConstructor
public enum DisputeHandleResultEnum {

    PUBLISHER_WIN(1, "发布者胜诉"),
    RUNNER_WIN(2, "跑腿者胜诉"),
    UNCERTAIN(3, "存在疑问");

    private final Integer code;
    private final String desc;

    public static DisputeHandleResultEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DisputeHandleResultEnum result : values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        return null;
    }
} 