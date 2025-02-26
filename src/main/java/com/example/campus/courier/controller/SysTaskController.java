package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "任务相关接口")
@RequiredArgsConstructor
@Slf4j
public class SysTaskController extends BaseController {

    private final SysTaskService sysTaskService;

    /**
     * 发布任务
     *
     * @param request 任务添加请求
     * @return 操作结果
     */
    @PostMapping("/publish")
    @Operation(summary = "发布任务")
    public Result<Boolean> publishTask(@RequestBody @Validated TaskAddRequest request) {
        return Result.success(sysTaskService.saveTask(request, getLoginUserId()));
    }

    /**
     * 查询当前用户发布的任务
     *
     * @param request 查询条件
     * @return 任务列表分页结果
     */
    @GetMapping("/my-published")
    @Operation(summary = "查询当前用户发布的任务")
    public Result<PageResult<TaskDTO>> getMyPublishedTasks(
            @Parameter(description = "查询条件") @Validated TaskQueryRequest request) {
        return Result.success(sysTaskService.getPublishedTasks(request, getLoginUserId()));
    }
} 