package com.example.campus.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.Task;
import com.example.campus.club.model.vo.TaskDetailVO;
import com.example.campus.club.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务管理")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController extends BaseController {

    private final TaskService taskService;

    @Operation(summary = "搜索任务")
    @GetMapping
    public Result<Page<Task>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long typeId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(taskService.search(keyword, status, typeId, pageNum, pageSize));
    }

    @Operation(summary = "获取任务详情")
    @GetMapping("/{id}")
    public Result<TaskDetailVO> getDetail(@PathVariable Long id) {
        return Result.success(taskService.getDetail(id));
    }
} 