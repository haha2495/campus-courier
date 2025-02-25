package com.example.campus.club.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "用户角色枚举")
public enum UserRoleEnum {

    ADMIN(1, "管理员"),
    STUDENT(4, "学生用户");

    @EnumValue
    private final Integer code;

    private final String desc;

    UserRoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据角色编码获取对应的枚举值
     *
     * @param code 角色编码
     * @return 角色枚举值
     */
    public static UserRoleEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserRoleEnum role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }
} 