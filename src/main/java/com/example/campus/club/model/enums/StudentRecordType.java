package com.example.campus.club.model.enums;

import lombok.Getter;

@Getter
public enum StudentRecordType {
    CREATE_ACCOUNT(1, "创建账号"),
    RESET_PASSWORD(2, "重置密码"),
    ENABLE_ACCOUNT(3, "启用账号"),
    DISABLE_ACCOUNT(4, "禁用账号"),
    ;

    private final Integer code;
    private final String desc;

    StudentRecordType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
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