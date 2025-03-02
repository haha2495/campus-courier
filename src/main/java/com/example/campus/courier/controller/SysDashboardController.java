package com.example.campus.courier.controller;

import com.example.campus.courier.model.dto.DashboardStatDTO;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "首页仪表盘接口")
@RequiredArgsConstructor
@Slf4j
public class SysDashboardController extends BaseController {

    private final SysDashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "获取首页统计数据")
    public Result<DashboardStatDTO> getDashboardStats() {
        return Result.success(dashboardService.getDashboardStats());
    }
} 