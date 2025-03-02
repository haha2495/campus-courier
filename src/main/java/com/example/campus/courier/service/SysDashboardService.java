package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.DashboardStatDTO;
import com.example.campus.courier.model.dto.TaskTypeStatDTO;

import java.util.List;

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

    /**
     * 获取任务类型统计数据
     * 包括每种类型的：
     * - 任务数量
     * - 占总任务的百分比
     *
     * @return 任务类型统计列表
     */
    List<TaskTypeStatDTO> getTaskTypeStats();
} 