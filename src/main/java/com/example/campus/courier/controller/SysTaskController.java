package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.dto.TaskDetailDTO;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.request.task.TaskMessageAddRequest;
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

import java.util.List;

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

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/cancel")
    @Operation(summary = "取消任务")
    public Result<Boolean> cancelTask(
            @Parameter(description = "任务ID") 
            @PathVariable("taskId") Long taskId) {
        return Result.success(sysTaskService.cancelTask(taskId, getLoginUserId()));
    }

    /**
     * 获取抢单大厅任务列表
     * 查询所有待接单的任务，按发布时间倒序排序
     *
     * @return 任务列表
     */
    @GetMapping("/hall")
    @Operation(summary = "获取抢单大厅任务列表")
    public Result<List<TaskDTO>> getTaskHallList() {
        return Result.success(sysTaskService.getTaskHallList(getLoginUserId()));
    }

    /**
     * 抢单（接受任务）
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/accept")
    @Operation(summary = "抢单（接受任务）")
    public Result<Boolean> acceptTask(
            @Parameter(description = "任务ID") 
            @PathVariable("taskId") Long taskId) {
        return Result.success(sysTaskService.acceptTask(taskId, getLoginUserId()));
    }

    /**
     * 获取我当前接单的任务列表
     * 只返回进行中的任务（已接单、进行中、待确认状态）
     *
     * @return 任务列表
     */
    @GetMapping("/my-accepted")
    @Operation(summary = "获取我当前接单的任务列表")
    public Result<List<TaskDTO>> getMyAcceptedTasks() {
        return Result.success(sysTaskService.getMyAcceptedTasks(getLoginUserId()));
    }

    /**
     * 开始执行任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/start")
    @Operation(summary = "开始执行任务")
    public Result<Boolean> startTask(
            @Parameter(description = "任务ID") 
            @PathVariable("taskId") Long taskId) {
        return Result.success(sysTaskService.startTask(taskId, getLoginUserId()));
    }

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    @GetMapping("/{taskId}")
    @Operation(summary = "获取任务详情")
    public Result<TaskDetailDTO> getTaskDetail(
            @Parameter(description = "任务ID") 
            @PathVariable("taskId") Long taskId) {
        return Result.success(sysTaskService.getTaskDetail(taskId));
    }

    /**
     * 添加任务留言
     *
     * @param request 留言请求
     * @return 操作结果
     */
    @PostMapping("/message")
    @Operation(summary = "添加任务留言")
    public Result<Boolean> addTaskMessage(@RequestBody @Validated TaskMessageAddRequest request) {
        return Result.success(sysTaskService.addTaskMessage(request, getLoginUserId()));
    }
} 