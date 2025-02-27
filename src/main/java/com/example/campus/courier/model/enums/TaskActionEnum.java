package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务操作枚举
 */
@Getter
@AllArgsConstructor
public enum TaskActionEnum {
    PUBLISH(1, "发布任务"),
    ACCEPT(2, "接受任务"),
    CANCEL(3, "取消任务"),
    START(4, "开始执行"),
    COMPLETE(5, "完成任务"),
    CONFIRM(6, "确认完成"),
    SUBMIT_DISPUTE(7, "提交纠纷"),
    RESOLVE_DISPUTE(8, "解决纠纷"),
    EVALUATE(9, "评价");

    private final Integer code;
    private final String desc;
} 