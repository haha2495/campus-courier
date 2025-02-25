package com.example.campus.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.TaskMessage;
import com.example.campus.club.model.vo.CourierTaskVO;
import com.example.campus.club.model.request.TaskMessageRequest;
import com.example.campus.club.service.CourierTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "跑腿任务接口")
@RestController
@RequestMapping("/api/courier/tasks")
@RequiredArgsConstructor
public class CourierTaskController extends BaseController {

    private final CourierTaskService courierTaskService;

    @Operation(summary = "获取抢单大厅任务列表")
    @GetMapping("/hall")
    public Result<Page<CourierTaskVO>> getTaskHallList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long typeId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(courierTaskService.getTaskHallList(keyword, typeId, pageNum, pageSize));
    }

    @Operation(summary = "获取任务详情")
    @GetMapping("/{taskId}")
    public Result<CourierTaskVO> getTaskDetail(@PathVariable Long taskId) {
        return Result.success(courierTaskService.getTaskDetail(taskId));
    }

    @Operation(summary = "接单")
    @PostMapping("/{taskId}/take")
    public Result<Void> takeTask(@PathVariable Long taskId) {
        courierTaskService.takeTask(taskId, getCurrentUserId());
        return Result.success();
    }

    @Operation(summary = "获取当前进行中的任务")
    @GetMapping("/current")
    public Result<Page<CourierTaskVO>> getCurrentTasks(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(courierTaskService.getCurrentTasks(getCurrentUserId(), pageNum, pageSize));
    }

    @Operation(summary = "获取历史接单列表")
    @GetMapping("/history")
    public Result<Page<CourierTaskVO>> getHistoryTasks(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(courierTaskService.getHistoryTasks(getCurrentUserId(), pageNum, pageSize));
    }

    @Operation(summary = "发送任务留言")
    @PostMapping("/messages")
    public Result<Void> sendMessage(@Validated @RequestBody TaskMessageRequest request) {
        courierTaskService.sendMessage(request, getCurrentUserId());
        return Result.success();
    }

    @Operation(summary = "获取任务留言列表")
    @GetMapping("/{taskId}/messages")
    public Result<Page<TaskMessage>> getTaskMessages(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(courierTaskService.getTaskMessages(taskId, pageNum, pageSize));
    }
} 