package com.example.campus.club.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.club.mapper.SysAdminMapper;
import com.example.campus.club.model.entity.SysAdmin;
import com.example.campus.club.model.request.AdminPageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import cn.dev33.satoken.stp.StpUtil;

@Tag(name = "系统管理员处理器")
@Component
@RequiredArgsConstructor
public class SysAdminHandler extends ServiceImpl<SysAdminMapper, SysAdmin> {

    private final SysAdminMapper sysAdminMapper;

    /**
     * 根据用户名查询管理员信息
     *
     * @param username 用户名
     * @return 管理员信息
     */
    public SysAdmin getAdminByUsername(String username) {
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAdmin::getUsername, username);
        return sysAdminMapper.selectOne(queryWrapper);
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return true-存在，false-不存在
     */
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAdmin::getUsername, username);
        return sysAdminMapper.exists(queryWrapper);
    }

    /**
     * 分页查询管理员列表
     *
     * @param request 查询参数
     * @return 分页结果
     */
    public Page<SysAdmin> page(AdminPageRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(request.getUsername()), SysAdmin::getUsername, request.getUsername())
                .like(StringUtils.isNotBlank(request.getNickname()), SysAdmin::getNickname, request.getNickname())
                .eq(request.getStatus() != null, SysAdmin::getStatus, request.getStatus())
                .orderByDesc(SysAdmin::getCreateTime);

        // 执行分页查询
        return sysAdminMapper.selectPage(
            new Page<>(request.getPageNum(), request.getPageSize()),
            queryWrapper
        );
    }

    /**
     * 更新管理员信息
     *
     * @param id 管理员ID
     * @param nickname 昵称
     * @param status 状态
     * @return 是否更新成功
     */
    public boolean updateAdmin(Integer id, String nickname, Boolean status) {
        LambdaUpdateWrapper<SysAdmin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysAdmin::getId, id)
                .set(SysAdmin::getNickname, nickname)
                .set(SysAdmin::getStatus, status);
        
        return update(null, updateWrapper);
    }

    /**
     * 删除管理员
     *
     * @param id 管理员ID
     * @return 是否删除成功
     */
    public boolean deleteAdmin(Integer id) {
        return removeById(id);
    }

    /**
     * 踢出指定管理员的登录状态
     *
     * @param id 管理员ID
     */
    public void kickOutLogin(Integer id) {
        // 踢出所有登录状态
        StpUtil.kickout(id);
    }

    /**
     * 重置管理员密码
     *
     * @param id 管理员ID
     * @param password 新密码（已加密）
     * @return 是否重置成功
     */
    public boolean resetPassword(Integer id, String password) {
        LambdaUpdateWrapper<SysAdmin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysAdmin::getId, id)
                .set(SysAdmin::getPassword, password);
        
        return update(null, updateWrapper);
    }
}