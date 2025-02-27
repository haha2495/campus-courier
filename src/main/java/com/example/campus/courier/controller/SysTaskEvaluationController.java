package com.example.campus.courier.controller;

import com.example.campus.courier.service.SysTaskEvaluationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

} 