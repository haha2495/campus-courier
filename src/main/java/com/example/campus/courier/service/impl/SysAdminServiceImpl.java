package com.example.campus.courier.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.exception.BusinessException;
import com.example.campus.courier.mapper.handler.SysAdminHandler;
import com.example.campus.courier.model.entity.SysAdmin;
import com.example.campus.courier.model.enums.UserRoleEnum;
import com.example.campus.courier.model.request.AdminAddRequest;
import com.example.campus.courier.model.request.AdminPageRequest;
import com.example.campus.courier.model.request.AdminUpdateRequest;
import com.example.campus.courier.model.request.AdminProfileUpdateRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.service.SysAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

@Tag(name = "系统管理员服务实现")
@Service
@RequiredArgsConstructor
@Slf4j
public class SysAdminServiceImpl implements SysAdminService {

    private final SysAdminHandler sysAdminHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdminAddRequest request) {
        // 1. 检查用户名是否已存在
        if (sysAdminHandler.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 构建并初始化管理员实体
        SysAdmin admin = new SysAdmin().init(
                request.getUsername(),
                request.getPassword(),
                request.getNickname(),
                request.getAvatar(),
                request.getStatus()
        );

        // 3. 保存管理员信息
        sysAdminHandler.save(admin);

        log.info("新增管理员成功，用户名：{}", request.getUsername());
    }

    @Override
    public String login(String username, String password) {
        // 根据用户名查询管理员
        SysAdmin admin = sysAdminHandler.getAdminByUsername(username);

        // 用户不存在
        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 账号被禁用
        if (!admin.getStatus()) {
            throw new BusinessException("账号已被禁用");
        }

        // 密码加密比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!encryptedPassword.equals(admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 登录成功，使用Sa-Token记录登录状态，在JWT中存储角色信息
        StpUtil.login(admin.getId(), new SaLoginModel()
                .setExtra("role", UserRoleEnum.ADMIN.getCode())
                .setIsWriteHeader(true));

        // 返回token
        return StpUtil.getTokenValue();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public PageResult<SysAdmin> page(AdminPageRequest request) {
        // 使用handler执行分页查询
        Page<SysAdmin> page = sysAdminHandler.page(request);

        // 构建返回结果
        return PageResult.build(page.getTotal(), page.getRecords());
    }

    @Override
    public SysAdmin getCurrentAdmin(Integer loginUserId) {
        // 查询管理员信息
        SysAdmin admin = sysAdminHandler.getById(loginUserId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        return admin;
    }

    @Override
    public SysAdmin getById(Integer id) {
        SysAdmin admin = sysAdminHandler.getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        // 清空密码
        admin.setPassword(null);
        return admin;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdminUpdateRequest request) {
        // 1. 检查管理员是否存在
        SysAdmin admin = sysAdminHandler.getById(request.getId());
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 执行更新
        if (!sysAdminHandler.updateAdmin(request.getId(), request.getNickname(), request.getStatus())) {
            throw new BusinessException("修改管理员失败");
        }

        // 3. 如果是当前登录账号被禁用，则踢出登录
        if (!request.getStatus()) {
            sysAdminHandler.kickOutLogin(request.getId());
        }

        log.info("修改管理员成功，ID：{}", request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        // 1. 检查管理员是否存在
        SysAdmin admin = sysAdminHandler.getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 执行删除
        if (!sysAdminHandler.deleteAdmin(id)) {
            throw new BusinessException("删除管理员失败");
        }

        sysAdminHandler.kickOutLogin(id);

        log.info("删除管理员成功，ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Integer id) {
        // 1. 检查管理员是否存在
        SysAdmin admin = sysAdminHandler.getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 重置密码为123456
        String newPassword = DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
        if (!sysAdminHandler.resetPassword(id, newPassword)) {
            throw new BusinessException("重置密码失败");
        }

        // 3. 踢出登录
        sysAdminHandler.kickOutLogin(id);

        log.info("重置管理员密码成功，ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Integer loginUserId, AdminProfileUpdateRequest request) {
        // 1. 检查管理员是否存在
        SysAdmin admin = sysAdminHandler.getById(loginUserId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 更新基本信息
        admin.setNickname(request.getNickname());
        admin.setAvatar(request.getAvatar());

        // 3. 如果提供了新密码，则更新密码
        if (StringUtils.hasText(request.getPassword())) {
            String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
            admin.setPassword(encryptedPassword);
        }

        // 4. 保存更新
        if (!sysAdminHandler.updateById(admin)) {
            throw new BusinessException("更新个人信息失败");
        }

        log.info("更新管理员个人信息成功，ID：{}", loginUserId);
    }

} 