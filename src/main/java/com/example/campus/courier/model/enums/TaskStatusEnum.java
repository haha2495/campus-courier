package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    PENDING(0, "待接单"),
    ACCEPTED(1, "已接单"),
    IN_PROGRESS(2, "进行中"),
    AWAITING_CONFIRMATION(3, "待确认"),
    COMPLETED(4, "已完成"),
    CANCELED(5, "已取消"),
    EXPIRED(6, "已过期"),
    DISPUTED(7, "存在纠纷"),
    EVALUATED(8, "已评价");

    private final Integer code;
    private final String desc;
} 