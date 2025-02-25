package com.example.campus.club.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.campus.club.common.utils.PinyinUtils;
import com.example.campus.club.mapper.handler.SysStudentRecordHandler;
import com.example.campus.club.model.dto.DepartmentDTO;
import com.example.campus.club.model.entity.SysStudent;
import com.example.campus.club.model.enums.StudentRecordType;
import com.example.campus.club.model.excel.StudentImportExcel;
import com.example.campus.club.model.response.ImportResult;
import com.example.campus.club.service.SysStudentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StudentImportListener extends AnalysisEventListener<StudentImportExcel> {

    private final SysStudentService studentService;
    private final ImportResult importResult;
    private final SysStudentRecordHandler sysStudentRecordHandler;
    private final List<SysStudent> successList = new ArrayList<>();


    // 缓存院系信息，避免重复查询
    private List<DepartmentDTO> departmentCache;
    private Set<String> existingStudentNos;

    @Override
    public void invoke(StudentImportExcel data, AnalysisContext context) {
        // 当前处理的行号（从0开始）
        int rowIndex = context.readRowHolder().getRowIndex();
        importResult.incrementTotal();

        // 校验数据
        String error = validateData(data);
        if (StringUtils.isNotBlank(error)) {
            importResult.addError(rowIndex + 1, data.getStudentNo(), error);
            return;
        }

        // 转换为学生实体
        try {
            SysStudent student = convertToEntity(data);
            successList.add(student);
            importResult.incrementSuccess();
        } catch (Exception e) {
            importResult.addError(rowIndex + 1, data.getStudentNo(), "数据转换失败：" + e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 只有在没有任何错误的情况下才保存数据
        if (importResult.getFailCount() == 0 && !successList.isEmpty()) {
            studentService.saveBatch(successList);
            for (SysStudent sysStudent : successList) {
                // 添加创建账号记录
                sysStudentRecordHandler.createRecord(
                        sysStudent.getId(),
                        StudentRecordType.CREATE_ACCOUNT,
                        String.format("创建账号，初始密码为：%s", getDefaultPassword(sysStudent.getName(), sysStudent.getStudentNo()))
                );
            }
            importResult.setSuccess(true);
        }
        importResult.complete();
    }

    private String validateData(StudentImportExcel data) {
        // 1. 基础数据校验
        if (StringUtils.isBlank(data.getStudentNo())) {
            return "学号不能为空";
        }
        if (StringUtils.isBlank(data.getName())) {
            return "姓名不能为空";
        }
        if (StringUtils.isBlank(data.getGender())) {
            return "性别不能为空";
        }
        if (!data.getGender().equals("男") && !data.getGender().equals("女")) {
            return "性别只能是'男'或'女'";
        }
        if (StringUtils.isBlank(data.getDepartment())) {
            return "院系不能为空";
        }
        if (StringUtils.isBlank(data.getMajor())) {
            return "专业不能为空";
        }
        if (StringUtils.isBlank(data.getGrade())) {
            return "年级不能为空";
        }
        if (StringUtils.isBlank(data.getClassName())) {
            return "班级不能为空";
        }
        if (StringUtils.isBlank(data.getStatus())) {
            return "状态不能为空";
        }
        if (!data.getStatus().equals("正常") && !data.getStatus().equals("禁用")) {
            return "状态只能是'正常'或'禁用'";
        }

        // 2. 学号唯一性校验
        if (existingStudentNos == null) {
            existingStudentNos = studentService.list().stream()
                    .map(SysStudent::getStudentNo)
                    .collect(Collectors.toSet());
        }
        // 检查数据库中是否存在
        if (existingStudentNos.contains(data.getStudentNo())) {
            return "学号已存在";
        }
        // 检查Excel中是否重复
        if (successList.stream().anyMatch(s -> s.getStudentNo().equals(data.getStudentNo()))) {
            return "Excel中存在重复学号";
        }

        // 3. 院系专业年级班级关联校验
        if (departmentCache == null) {
            departmentCache = studentService.getDepartments();
        }

        // 查找对应的院系
        DepartmentDTO department = departmentCache.stream()
                .filter(dept -> dept.getName().equals(data.getDepartment()))
                .findFirst()
                .orElse(null);
        if (department == null) {
            return "院系不存在";
        }

        // 查找对应的专业
        DepartmentDTO.MajorDTO major = department.getMajors().stream()
                .filter(m -> m.getName().equals(data.getMajor()))
                .findFirst()
                .orElse(null);
        if (major == null) {
            return "专业不存在或不属于该院系";
        }

        // 查找对应的年级
        DepartmentDTO.GradeDTO grade = major.getGrades().stream()
                .filter(g -> g.getName().equals(data.getGrade()))
                .findFirst()
                .orElse(null);
        if (grade == null) {
            return "年级不存在或不属于该专业";
        }

        // 查找对应的班级
        if (!grade.getClasses().contains(data.getClassName())) {
            return "班级不存在或不属于该年级";
        }

        return null;
    }

    private SysStudent convertToEntity(StudentImportExcel data) {
        SysStudent student = new SysStudent();

        // 设置默认值
        String defaultPassword = getDefaultPassword(data.getName(), data.getStudentNo());
        Integer gender = "男".equals(data.getGender()) ? 1 : 2;
        Integer enrollmentYear = Integer.parseInt(data.getGrade().substring(0, 4));
        Boolean status = "正常".equals(data.getStatus());

        return student.init(
                data.getStudentNo(),
                defaultPassword,
                data.getName(),
                gender,
                data.getDepartment(),
                data.getMajor(),
                data.getGrade(),
                data.getClassName(),
                enrollmentYear,
                status, // 根据Excel中的状态设置
                null, // 默认无头像
                data.getEmail()
        );
    }

    private String getDefaultPassword(String name, String studentNo) {
        String firstLetter = PinyinUtils.getFirstLetter(name);
        return studentNo + firstLetter;
    }
} 