package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionEnum {
    PUBLISH(1, "发布"),
    ACCEPT(2, "接单"),
    START_EXECUTION(3, "开始执行"),
    COMPLETE_TASK(4, "完成任务"),
    CONFIRM_COMPLETION(5, "确认完成"),
    CANCEL(6, "取消任务"),
    SUBMIT_DISPUTE(7, "提交纠纷"),
    RESOLVE_DISPUTE(8, "解决纠纷"),
    EVALUATE(9, "评价");

    private final int code;
    private final String description;
} 