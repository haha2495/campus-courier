package com.example.campus.club.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.club.mapper.SysStudentMapper;
import com.example.campus.club.model.entity.SysStudent;
import com.example.campus.club.model.enums.UserRoleEnum;
import com.example.campus.club.model.request.StudentPageRequest;
import com.example.campus.club.model.request.StudentUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Tag(name = "学生信息处理器")
@Component
@RequiredArgsConstructor
public class SysStudentHandler extends ServiceImpl<SysStudentMapper, SysStudent> {

    /**
     * 检查学号是否已存在
     *
     * @param studentNo 学号
     * @return true: 存在, false: 不存在
     */
    public boolean existsByStudentNo(String studentNo) {
        LambdaQueryWrapper<SysStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudent::getStudentNo, studentNo);
        return count(wrapper) > 0;
    }

    /**
     * 分页查询学生列表
     *
     * @param request 查询参数
     * @return 分页结果
     */
    public Page<SysStudent> page(StudentPageRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<SysStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getStudentNo()), SysStudent::getStudentNo, request.getStudentNo())
                .like(StringUtils.isNotBlank(request.getName()), SysStudent::getName, request.getName())
                .like(StringUtils.isNotBlank(request.getDepartment()), SysStudent::getDepartment, request.getDepartment())
                .like(StringUtils.isNotBlank(request.getMajor()), SysStudent::getMajor, request.getMajor())
                .like(StringUtils.isNotBlank(request.getGrade()), SysStudent::getGrade, request.getGrade())
                .like(StringUtils.isNotBlank(request.getClassName()), SysStudent::getClassName, request.getClassName())
                .eq(request.getIdentityType() != null, SysStudent::getIdentityType, request.getIdentityType())
                .eq(request.getStatus() != null, SysStudent::getStatus, request.getStatus())
                .orderByDesc(SysStudent::getCreateTime);

        // 执行分页查询
        return page(new Page<>(request.getPageNum(), request.getPageSize()), wrapper);
    }

    /**
     * 更新学生信息（学号不可修改）
     *
     * @param request 更新请求
     * @return 是否更新成功
     */
    public boolean updateStudent(StudentUpdateRequest request) {
        LambdaUpdateWrapper<SysStudent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysStudent::getId, request.getId())
                .set(request.getName() != null, SysStudent::getName, request.getName())
                .set(request.getGender() != null, SysStudent::getGender, request.getGender())
                .set(SysStudent::getAvatar, request.getAvatar())
                .set(request.getEmail() != null, SysStudent::getEmail, request.getEmail())
                .set(request.getDepartment() != null, SysStudent::getDepartment, request.getDepartment())
                .set(request.getMajor() != null, SysStudent::getMajor, request.getMajor())
                .set(request.getGrade() != null, SysStudent::getGrade, request.getGrade())
                .set(request.getClassName() != null, SysStudent::getClassName, request.getClassName())
                .set(request.getEnrollmentYear() != null, SysStudent::getEnrollmentYear, request.getEnrollmentYear())
                .set(request.getStatus() != null, SysStudent::getStatus, request.getStatus());

        return update(updateWrapper);
    }

    public SysStudent getStudentByStudentNo(String studentNo) {
        LambdaQueryWrapper<SysStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudent::getStudentNo, studentNo);
        wrapper.eq(SysStudent::getIdentityType, UserRoleEnum.STUDENT.getCode());
        return baseMapper.selectOne(wrapper);
    }

    public SysStudent getByStudentNo(String studentNo) {
        LambdaQueryWrapper<SysStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudent::getStudentNo, studentNo);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 更新学生身份类型
     *
     * @param studentId 学生ID
     * @param role      用户角色
     */
    public void updateIdentityType(Integer studentId, UserRoleEnum role) {
        LambdaUpdateWrapper<SysStudent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysStudent::getId, studentId)
                .set(SysStudent::getIdentityType, role.getCode());
        update(updateWrapper);
    }
}