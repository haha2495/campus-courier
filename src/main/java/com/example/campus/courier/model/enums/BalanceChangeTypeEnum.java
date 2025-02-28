package com.example.campus.courier.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceChangeTypeEnum {

    RECHARGE(1, "充值"),
    WITHDRAW(2, "提现"),
    INCOME(3, "收入"),
    EXPENSE(4, "支出");

    private final Integer code;
    private final String desc;

    public static String getDesc(Integer code) {
        for (BalanceChangeTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
} 