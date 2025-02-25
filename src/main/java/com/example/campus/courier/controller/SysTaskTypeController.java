package com.example.campus.courier.controller;

import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
import com.example.campus.courier.model.request.task.TaskTypePageRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysTaskTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务类型控制器
 */
@RestController
@RequestMapping("/api/task/types")
@RequiredArgsConstructor
@Tag(name = "任务类型管理", description = "任务类型相关接口")
public class SysTaskTypeController extends BaseController {

    private final SysTaskTypeService taskTypeService;

    /**
     * 添加任务类型
     *
     * @param request 任务类型添加请求
     * @return 响应结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加任务类型", description = "添加新的任务类型")
    public Result<Boolean> addTaskType(
            @RequestBody @Validated @Parameter(description = "任务类型信息") TaskTypeAddRequest request) {
        // 检查当前用户是否为管理员
        if (!isAdmin()) {
            return Result.forbidden();
        }
        return Result.success(taskTypeService.addTaskType(request), "添加任务类型成功");
    }
    
    /**
     * 分页查询任务类型
     *
     * @param request 分页查询请求
     * @return 分页数据
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询任务类型", description = "支持按状态筛选，按优先级和创建时间倒序排列")
    public Result<PageResult<SysTaskType>> pageTaskTypes(
            @RequestBody @Validated @Parameter(description = "分页查询参数") TaskTypePageRequest request) {
        // 检查当前用户是否为管理员
        if (!isAdmin()) {
            return Result.forbidden();
        }
        return Result.success(taskTypeService.pageTaskTypes(request), "查询成功");
    }
} 