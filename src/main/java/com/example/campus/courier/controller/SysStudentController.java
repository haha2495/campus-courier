package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.DepartmentDTO;
import com.example.campus.courier.model.dto.StudentRecordDTO;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.request.student.*;
import com.example.campus.courier.model.response.ImportResult;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "学生信息接口")
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class SysStudentController extends BaseController {

    private final SysStudentService sysStudentService;

    @Operation(summary = "获取院系专业年级信息")
    @GetMapping("/departments")
    public Result<List<DepartmentDTO>> getDepartments() {
        return Result.success(sysStudentService.getDepartments());
    }

    @PostMapping("/add")
    @Operation(summary = "新增学生")
    public Result<Void> addStudent(@Valid @RequestBody StudentAddRequest request) {
        sysStudentService.addStudent(request);
        return Result.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询学生列表")
    public Result<PageResult<SysStudent>> page(@Valid @RequestBody StudentPageRequest request) {
        return Result.success(sysStudentService.page(request));
    }

    @GetMapping("/template")
    @Operation(summary = "下载学生导入模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        sysStudentService.downloadTemplate(response);
    }

    @PostMapping("/import")
    @Operation(summary = "导入学生信息")
    public Result<ImportResult> importStudents(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(sysStudentService.importStudents(file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询学生信息")
    public Result<SysStudent> getById(@PathVariable("id") Integer id) {
        return Result.success(sysStudentService.getById(id));
    }

    @PostMapping("/update")
    @Operation(summary = "更新学生信息")
    public Result<Void> update(@Valid @RequestBody StudentUpdateRequest request) {
        sysStudentService.update(request);
        return Result.success();
    }

    @PostMapping("/{id}/reset-password")
    @Operation(summary = "重置学生密码")
    public Result<Void> resetPassword(@PathVariable("id") Integer id) {
        sysStudentService.resetPassword(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public Result<Void> delete(@PathVariable("id") Integer id) {
        sysStudentService.delete(id);
        return Result.success();
    }

    @GetMapping("/records/{studentId}")
    @Operation(summary = "获取学生记录")
    public Result<List<StudentRecordDTO>> getStudentRecords(
            @Parameter(description = "学生ID") @PathVariable Long studentId
    ) {
        return Result.success(sysStudentService.getStudentRecords(studentId));
    }

    @GetMapping("/getByStudentNo")
    @Operation(summary = "根据学号精准查询学生信息")
    @Parameters({
            @Parameter(name = "studentNo", description = "学号", required = true)
    })
    public Result<SysStudent> getByStudentNo(@RequestParam String studentNo) {
        SysStudent student = sysStudentService.getByStudentNo(studentNo);
        if (student == null) {
            return Result.failed("未找到该学号对应的学生信息");
        }
        return Result.success(student);
    }

    @Operation(summary = "获取当前学生信息")
    @GetMapping("/current")
    public Result<SysStudent> getCurrentStudent() {
        return Result.success(sysStudentService.getCurrentStudent(getLoginUserId()));
    }

    @Operation(summary = "学生登录")
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody StudentLoginRequest request) {
        String token = sysStudentService.login(request.getStudentNo(), request.getPassword());
        return Result.success(token);
    }

    @Operation(summary = "学生退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        sysStudentService.logout();
        return Result.success();
    }

    @Operation(summary = "更新当前学生个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(
            @Parameter(description = "个人信息", required = true)
            @Valid @RequestBody StudentProfileUpdateRequest request
    ) {
        sysStudentService.updateProfile(getLoginUserId(), request);
        return Result.success();
    }
} 