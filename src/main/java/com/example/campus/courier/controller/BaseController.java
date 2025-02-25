package com.example.campus.courier.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.campus.courier.model.enums.UserRoleEnum;

/**
 * 基础控制器
 * 提供获取当前登录用户信息的通用方法
 */
public abstract class BaseController {
    

    /**
     * 获取当前登录用户ID
     */
    protected Integer getLoginUserId() {
        return StpUtil.getLoginIdAsInt();
    }
    
    /**
     * 获取当前登录用户角色
     */
    protected UserRoleEnum getLoginUserRole() {
        Object roleObj = StpUtil.getExtra("role");
        if (roleObj == null) {
            return null;
        }
        // 先转换为Number，再获取int值
        if (roleObj instanceof Number) {
            return UserRoleEnum.getByCode(((Number) roleObj).intValue());
        }
        return null;
    }
    
    /**
     * 判断当前用户是否具有指定角色
     */
    protected boolean hasRole(UserRoleEnum role) {
        return role.equals(getLoginUserRole());
    }
    
    /**
     * 判断当前用户是否为管理员
     */
    protected boolean isAdmin() {
        return hasRole(UserRoleEnum.ADMIN);
    }

} 