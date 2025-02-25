package com.example.campus.courier.controller;

import com.example.campus.courier.service.SysTaskTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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


} 