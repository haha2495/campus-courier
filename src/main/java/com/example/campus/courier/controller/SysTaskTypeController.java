package com.example.campus.courier.controller;

import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
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
} 