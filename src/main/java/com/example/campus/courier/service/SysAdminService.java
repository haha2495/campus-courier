package com.example.campus.courier.service;

import com.example.campus.courier.model.request.admin.AdminAddRequest;
import com.example.campus.courier.model.request.admin.AdminPageRequest;
import com.example.campus.courier.model.request.admin.AdminUpdateRequest;
import com.example.campus.courier.model.request.admin.AdminProfileUpdateRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.entity.SysAdmin;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "系统管理员服务")
public interface SysAdminService {
    
    /**
     * 获取当前登录管理员信息
     *
     * @return 管理员信息
     */
    SysAdmin getCurrentAdmin(Integer loginUserId);
    
    /**
     * 管理员登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token值
     */
    String login(String username, String password);

    /**
     * 管理员退出登录
     */
    void logout();

    /**
     * 新增管理员
     *
     * @param request 新增管理员请求参数
     */
    void add(AdminAddRequest request);

    /**
     * 分页查询管理员列表
     *
     * @param request 查询参数
     * @return 分页结果
     */
    PageResult<SysAdmin> page(AdminPageRequest request);

    /**
     * 根据ID查询管理员信息
     *
     * @param id 管理员ID
     * @return 管理员信息
     */
    SysAdmin getById(Integer id);

    /**
     * 修改管理员信息
     *
     * @param request 修改管理员请求参数
     */
    void update(AdminUpdateRequest request);

    /**
     * 删除管理员
     *
     * @param id 管理员ID
     */
    void delete(Integer id);

    /**
     * 重置管理员密码
     *
     * @param id 管理员ID
     */
    void resetPassword(Integer id);

    /**
     * 更新当前管理员个人信息
     *
     * @param loginUserId 当前登录用户ID
     * @param request 更新请求
     */
    void updateProfile(Integer loginUserId, AdminProfileUpdateRequest request);
} 