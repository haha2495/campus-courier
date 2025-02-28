package com.example.campus.courier.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum StudentRecordType {
    CREATE_ACCOUNT(1, "创建账号"),
    RESET_PASSWORD(2, "重置密码"),
    ENABLE_ACCOUNT(3, "启用账号"),
    DISABLE_ACCOUNT(4, "禁用账号"),
    CHANGE_PASSWORD(5, "修改密码");

    @EnumValue
    private final Integer code;
    private final String description;

    StudentRecordType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举实例
     *
     * @param code 类型编码
     * @return 对应的枚举实例，如果不存在则返回null
     */
    public static StudentRecordType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (StudentRecordType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 