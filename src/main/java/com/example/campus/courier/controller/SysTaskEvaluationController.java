package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.evaluation.EvaluationListDTO;
import com.example.campus.courier.model.request.evaluation.EvaluationAddRequest;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysTaskEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务评价控制器
 */
@RestController
@RequestMapping("/api/task-evaluations")
@Tag(name = "任务评价相关接口")
@RequiredArgsConstructor
@Slf4j
public class SysTaskEvaluationController extends BaseController {

    private final SysTaskEvaluationService sysTaskEvaluationService;

    /**
     * 添加任务评价
     *
     * @param request 评价请求
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "添加任务评价")
    public Result<Boolean> addEvaluation(@RequestBody @Validated EvaluationAddRequest request) {
        log.info("添加任务评价，request: {}", request);
        return Result.success(sysTaskEvaluationService.addEvaluation(request, getLoginUserId()));
    }

    /**
     * 查询学生收到的评价列表
     *
     * @return 评价列表数据
     */
    @GetMapping("/received")
    @Operation(summary = "查询收到的评价列表")
    public Result<EvaluationListDTO> getReceivedEvaluations() {
        log.info("查询学生收到的评价列表，studentId: {}", getLoginUserId());
        return Result.success(sysTaskEvaluationService.getReceivedEvaluations(getLoginUserId()));
    }
} 