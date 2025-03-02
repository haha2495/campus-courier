package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.DashboardStatDTO;

/**
 * 首页仪表盘服务接口
 */
public interface SysDashboardService {

    /**
     * 获取首页统计数据
     * 包括：
     * - 总任务数及增长率
     * - 活跃用户数及增长率
     * - 总收入及增长率
     * - 任务完成率及变化
     *
     * @return 统计数据DTO
     */
    DashboardStatDTO getDashboardStats();
} 