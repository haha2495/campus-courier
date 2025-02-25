package com.example.campus.club.controller;

import com.example.campus.club.model.entity.SysAdmin;
import com.example.campus.club.model.request.AdminAddRequest;
import com.example.campus.club.model.request.AdminLoginRequest;
import com.example.campus.club.model.request.AdminPageRequest;
import com.example.campus.club.model.request.AdminUpdateRequest;
import com.example.campus.club.model.request.AdminProfileUpdateRequest;
import com.example.campus.club.model.response.PageResult;
import com.example.campus.club.model.response.Result;
import com.example.campus.club.service.SysAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统管理员接口")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SysAdminController extends BaseController {

    private final SysAdminService sysAdminService;
    
    @Operation(summary = "获取当前登录管理员信息")
    @GetMapping("/current")
    public Result<SysAdmin> getCurrentAdmin() {
        return Result.success(sysAdminService.getCurrentAdmin(getLoginUserId()));
    }
    
    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody AdminLoginRequest request) {
        String token = sysAdminService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }
    
    @Operation(summary = "管理员退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        sysAdminService.logout();
        return Result.success();
    }

    @Operation(summary = "分页查询管理员列表")
    @PostMapping("/page")
    public Result<PageResult<SysAdmin>> page(
            @Parameter(description = "查询参数", required = true)
            @Valid @RequestBody AdminPageRequest request
    ) {
        return Result.success(sysAdminService.page(request));
    }

    @Operation(summary = "新增管理员")
    @PostMapping("/add")
    public Result<Void> add(
            @Parameter(description = "管理员信息", required = true)
            @Valid @RequestBody AdminAddRequest request
    ) {
        sysAdminService.add(request);
        return Result.success();
    }

    @Operation(summary = "根据ID查询管理员信息")
    @GetMapping("/{id}")
    public Result<SysAdmin> getById(@PathVariable("id") Integer id) {
        return Result.success(sysAdminService.getById(id));
    }

    @Operation(summary = "修改管理员信息")
    @PostMapping("/update")
    public Result<Void> update(
            @Parameter(description = "管理员信息", required = true)
            @Valid @RequestBody AdminUpdateRequest request
    ) {
        sysAdminService.update(request);
        return Result.success();
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Integer id) {
        sysAdminService.delete(id);
        return Result.success();
    }

    @Operation(summary = "重置管理员密码")
    @PostMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable("id") Integer id) {
        sysAdminService.resetPassword(id);
        return Result.success();
    }

    @Operation(summary = "更新当前管理员个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(
            @Parameter(description = "个人信息", required = true)
            @Valid @RequestBody AdminProfileUpdateRequest request
    ) {
        sysAdminService.updateProfile(getLoginUserId(), request);
        return Result.success();
    }
} 