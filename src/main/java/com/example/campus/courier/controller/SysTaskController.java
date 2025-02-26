package com.example.campus.courier.controller;

import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "任务管理", description = "任务相关接口")
public class SysTaskController extends BaseController {

    private final SysTaskService sysTaskService;

    /**
     * 发布任务
     *
     * @param request 任务发布请求参数
     * @return 发布结果
     */
    @PostMapping("/publish")
    @Operation(summary = "发布任务", description = "用户发布新任务")
    public Result<Boolean> publishTask(@RequestBody @Valid TaskAddRequest request) {
        // 获取当前登录用户ID
        Integer currentUserId = getLoginUserId();
        
        // 调用服务保存任务
        boolean success = sysTaskService.saveTask(request, currentUserId);
        
        // 返回保存结果
        if (success) {
            return Result.success(true, "任务发布成功");
        } else {
            return Result.failed("任务发布失败");
        }
    }
} 