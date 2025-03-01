package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.request.dispute.DisputeHandleRequest;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysDisputeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 纠纷控制器
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispute")
@Tag(name = "纠纷管理接口")
public class SysDisputeController {

    private final SysDisputeService disputeService;

    @PostMapping("/create")
    @Operation(summary = "创建纠纷", description = "用户提交纠纷信息")
    public Result<Integer> createDispute(
            @Parameter(description = "纠纷创建请求", required = true)
            @Valid @RequestBody DisputeCreateRequest request
    ) {
        log.info("创建纠纷请求: {}", request);
        return Result.success(disputeService.createDispute(request), "纠纷提交成功");
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询纠纷任务", description = "管理员分页查询纠纷任务列表")
    public Result<PageResult<TaskDTO>> pageDisputes(
            @Parameter(description = "分页查询参数")
            @Valid DisputePageRequest request
    ) {
        log.info("分页查询纠纷任务请求: {}", request);
        PageResult<TaskDTO> page = disputeService.pageDisputeTasks(request);
        return Result.success(page);
    }

    @PostMapping("/handle")
    @Operation(summary = "处理纠纷", description = "管理员处理纠纷，根据结果进行相应处理")
    public Result<Void> handleDispute(
            @Parameter(description = "纠纷处理请求", required = true)
            @Valid @RequestBody DisputeHandleRequest request
    ) {
        log.info("处理纠纷请求: {}", request);
        disputeService.handleDispute(request);
        return Result.success();
    }

    @GetMapping("/hall/list")
    @Operation(summary = "查询纠纷大厅数据", description = "获取当前在纠纷大厅展示的任务列表")
    public Result<List<TaskDTO>> listHallDisputes() {
        log.info("查询纠纷大厅数据");
        List<TaskDTO> disputes = disputeService.listHallDisputes();
        return Result.success(disputes);
    }
} 