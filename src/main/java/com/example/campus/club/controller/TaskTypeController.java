package com.example.campus.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.TaskType;
import com.example.campus.club.model.request.TaskTypeRequest;
import com.example.campus.club.service.TaskTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务类型管理")
@RestController
@RequestMapping("/api/task-types")
@RequiredArgsConstructor
public class TaskTypeController extends BaseController {

    private final TaskTypeService taskTypeService;

    @Operation(summary = "新增任务类型")
    @PostMapping
    public Result<TaskType> add(@Validated @RequestBody TaskTypeRequest request) {
        return Result.success(taskTypeService.add(request));
    }

    @Operation(summary = "修改任务类型")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody TaskTypeRequest request) {
        taskTypeService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "删除任务类型")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskTypeService.delete(id);
        return Result.success();
    }

    @Operation(summary = "搜索任务类型")
    @GetMapping
    public Result<Page<TaskType>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(taskTypeService.search(keyword, pageNum, pageSize));
    }
} 