package com.example.campus.courier.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.common.utils.PinyinUtils;
import com.example.campus.courier.excel.StudentImportListener;
import com.example.campus.courier.exception.BusinessException;
import com.example.campus.courier.mapper.SysStudentMapper;
import com.example.campus.courier.mapper.handler.SysStudentHandler;
import com.example.campus.courier.mapper.handler.SysStudentRecordHandler;
import com.example.campus.courier.model.dto.DepartmentDTO;
import com.example.campus.courier.model.dto.StudentRecordDTO;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.enums.StudentRecordType;
import com.example.campus.courier.model.enums.UserRoleEnum;
import com.example.campus.courier.model.excel.StudentImportExcel;
import com.example.campus.courier.model.request.student.*;
import com.example.campus.courier.model.response.ImportResult;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.service.SysStudentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "学生信息服务实现")
@Service
@RequiredArgsConstructor
@Slf4j
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent> implements SysStudentService {

    private final SysStudentHandler sysStudentHandler;
    private final SysStudentRecordHandler sysStudentRecordHandler;
    private final ObjectMapper objectMapper;

    @Override
    public List<DepartmentDTO> getDepartments() {
        try {
            // 读取JSON文件
            ClassPathResource resource = new ClassPathResource("data/departments.json");
            Map<String, Object> data = objectMapper.readValue(resource.getInputStream(), new TypeReference<Map<String, Object>>() {
            });

            // 获取院系列表
            List<Map<String, Object>> departmentList = objectMapper.convertValue(data.get("departments"), new TypeReference<List<Map<String, Object>>>() {
            });

            // 转换为DTO对象
            List<DepartmentDTO> departments = new ArrayList<>();
            for (Map<String, Object> dept : departmentList) {
                DepartmentDTO department = new DepartmentDTO();
                department.setName((String) dept.get("name"));

                List<Map<String, Object>> majorList = objectMapper.convertValue(dept.get("majors"), new TypeReference<List<Map<String, Object>>>() {
                });
                List<DepartmentDTO.MajorDTO> majors = new ArrayList<>();

                for (Map<String, Object> major : majorList) {
                    DepartmentDTO.MajorDTO majorDTO = new DepartmentDTO.MajorDTO();
                    majorDTO.setName((String) major.get("name"));

                    // 获取年级列表
                    List<Map<String, Object>> gradeList = objectMapper.convertValue(major.get("grades"), new TypeReference<List<Map<String, Object>>>() {
                    });
                    List<DepartmentDTO.GradeDTO> grades = new ArrayList<>();

                    // 转换年级和班级信息
                    for (Map<String, Object> grade : gradeList) {
                        DepartmentDTO.GradeDTO gradeDTO = new DepartmentDTO.GradeDTO();
                        gradeDTO.setName((String) grade.get("name"));
                        gradeDTO.setClasses(objectMapper.convertValue(grade.get("classes"), new TypeReference<List<String>>() {
                        }));
                        grades.add(gradeDTO);
                    }

                    majorDTO.setGrades(grades);
                    majors.add(majorDTO);
                }

                department.setMajors(majors);
                departments.add(department);
            }

            return departments;
        } catch (IOException e) {
            log.error("读取院系信息失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(StudentAddRequest request) {
        // 1. 检查学号是否已存在
        if (sysStudentHandler.existsByStudentNo(request.getStudentNo())) {
            throw new BusinessException("该学号已存在");
        }

        // 2. 生成默认密码（学号 + 姓名首字母）
        String firstLetter = PinyinUtils.getFirstLetter(request.getName());
        String defaultPassword = request.getStudentNo() + firstLetter;

        // 3. 使用init方法构建并初始化学生实体
        SysStudent student = new SysStudent().init(
                request.getStudentNo(),
                defaultPassword,
                request.getName(),
                request.getGender(),
                request.getDepartment(),
                request.getMajor(),
                request.getGrade(),
                request.getClassName(),
                request.getEnrollmentYear(),
                request.getStatus(),
                request.getAvatar(),
                request.getEmail()
        );

        // 4. 保存学生信息
        sysStudentHandler.save(student);

        // 5. 添加创建账号记录
        sysStudentRecordHandler.createRecord(
                student.getId(),
                StudentRecordType.CREATE_ACCOUNT,
                String.format("创建账号，初始密码为：%s", defaultPassword)
        );
    }

    @Override
    public PageResult<SysStudent> page(StudentPageRequest request) {
        // 使用handler执行分页查询
        Page<SysStudent> page = sysStudentHandler.page(request);

        // 构建返回结果
        return PageResult.build(page.getTotal(), page.getRecords());
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName = URLEncoder.encode("学生导入模板", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 读取模板文件
        ClassPathResource resource = new ClassPathResource("template/学生导入模板.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            // 将文件内容写入响应流
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importStudents(MultipartFile file) throws IOException {
        // 创建导入结果对象
        ImportResult importResult = ImportResult.build(0);

        try {
            // 创建Excel读取监听器
            StudentImportListener listener = new StudentImportListener(this, importResult, sysStudentRecordHandler);

            // 读取Excel文件
            EasyExcel.read(file.getInputStream(), StudentImportExcel.class, listener)
                    .sheet()
                    .doRead();

            return importResult;
        } catch (Exception e) {
            log.error("导入学生信息失败", e);
            throw new BusinessException("导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentUpdateRequest request) {
        // 1. 检查学生是否存在
        SysStudent oldStudent = getById(request.getId());
        if (oldStudent == null) {
            throw new BusinessException("学生不存在");
        }

        // 2. 使用handler更新学生信息
        boolean success = sysStudentHandler.updateStudent(request);
        if (!success) {
            throw new BusinessException("更新学生信息失败");
        }

        // 3. 检查是否修改了状态
        if (request.getStatus() != null && !request.getStatus().equals(oldStudent.getStatus())) {
            sysStudentRecordHandler.createRecord(
                    request.getId(),
                    request.getStatus() ? StudentRecordType.ENABLE_ACCOUNT : StudentRecordType.DISABLE_ACCOUNT,
                    request.getStatus() ? "账号被启用" : "账号被禁用"
            );
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Integer id) {
        // 1. 获取学生信息
        SysStudent student = getById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }

        // 2. 生成新密码（学号 + 姓名首字母）
        String firstLetter = PinyinUtils.getFirstLetter(student.getName());
        String newPassword = student.getStudentNo() + firstLetter;

        // 3. 更新密码
        student.updatePassword(newPassword);
        updateById(student);

        // 4. 添加重置密码记录
        sysStudentRecordHandler.createRecord(
                id,
                StudentRecordType.RESET_PASSWORD,
                String.format("重置密码为：%s", newPassword)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        // 1. 检查学生是否存在
        SysStudent student = getById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }

        // 2. 删除学生信息
        if (!removeById(id)) {
            throw new BusinessException("删除学生失败");
        }
    }

    @Override
    public List<StudentRecordDTO> getStudentRecords(Long studentId) {
        return sysStudentRecordHandler.getStudentRecords(studentId);
    }

    @Override
    public SysStudent getByStudentNo(String studentNo) {
        return sysStudentHandler.getStudentByStudentNo(studentNo);
    }

    @Override
    public String login(String studentNo, String password) {
        // 根据学号查询学生
        SysStudent student = sysStudentHandler.getByStudentNo(studentNo);
        if (student == null) {
            throw new BusinessException("无当前学号");
        }

        // 账号被禁用
        if (!student.getStatus()) {
            throw new BusinessException("账号已被禁用");
        }

        // 密码加密比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!encryptedPassword.equals(student.getPassword())) {
            throw new BusinessException("密码错误");
        }


        // 学生登录，在JWT中存储角色信息
        StpUtil.login(student.getId(), new SaLoginModel()
                .setExtra("role", UserRoleEnum.STUDENT.getCode())
                .setIsWriteHeader(true));


        // 返回token
        return StpUtil.getTokenValue();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public SysStudent getCurrentStudent(Integer studentId) {
        SysStudent student = getById(studentId);
        if (student == null) {
            throw new BusinessException("学生信息不存在");
        }
        return student;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Integer studentId, StudentProfileUpdateRequest request) {
        // 1. 检查学生是否存在
        SysStudent student = getById(studentId);
        if (student == null) {
            throw new BusinessException("学生信息不存在");
        }

        // 2. 更新个人信息
        student.updateProfile(request);

        // 3. 保存更新
        if (!updateById(student)) {
            throw new BusinessException("更新个人信息失败");
        }

        log.info("更新学生个人信息成功，ID：{}", studentId);
    }

    @Override
    public void changePassword(Integer userId, StudentChangePasswordRequest request) {
        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("新密码和确认密码不一致");
        }

        // 获取当前学生信息
        SysStudent student = getById(userId);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }

        // 验证原密码是否正确
        String oldPasswordEncrypted = DigestUtils.md5DigestAsHex(request.getOldPassword().getBytes(StandardCharsets.UTF_8));
        if (!oldPasswordEncrypted.equals(student.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        student.updatePassword(request.getNewPassword());
        updateById(student);

        // 添加修改密码记录
        sysStudentRecordHandler.createRecord(
                userId,
                StudentRecordType.CHANGE_PASSWORD,
                "修改密码成功"
        );
    }
}