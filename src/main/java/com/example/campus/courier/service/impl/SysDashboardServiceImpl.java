package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.SysTaskMapper;
import com.example.campus.courier.mapper.SysStudentMapper;
import com.example.campus.courier.model.dto.DashboardStatDTO;
import com.example.campus.courier.model.dto.TaskTypeStatDTO;
import com.example.campus.courier.service.SysDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 首页仪表盘服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysDashboardServiceImpl implements SysDashboardService {

    private final SysTaskMapper taskMapper;
    private final SysStudentMapper studentMapper;

    @Override
    public DashboardStatDTO getDashboardStats() {
        DashboardStatDTO stats = new DashboardStatDTO();

        // 获取当前时间和30天前的时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sixtyDaysAgo = now.minusDays(60);

        // 计算总任务数和增长率
        Integer currentTaskCount = taskMapper.countTotalTasks(thirtyDaysAgo, now);
        Integer previousTaskCount = taskMapper.countTotalTasks(sixtyDaysAgo, thirtyDaysAgo);
        stats.setTotalTaskCount(currentTaskCount);
        stats.setTotalTaskGrowth(calculateGrowthRate(previousTaskCount, currentTaskCount));

        // 计算活跃用户数和增长率
        Integer currentActiveUsers = studentMapper.countActiveUsers(thirtyDaysAgo, now);
        Integer previousActiveUsers = studentMapper.countActiveUsers(sixtyDaysAgo, thirtyDaysAgo);
        stats.setActiveUserCount(currentActiveUsers);
        stats.setActiveUserGrowth(calculateGrowthRate(previousActiveUsers, currentActiveUsers));

        // 计算总收入和增长率
        Double currentIncome = taskMapper.sumTotalIncome(thirtyDaysAgo, now);
        Double previousIncome = taskMapper.sumTotalIncome(sixtyDaysAgo, thirtyDaysAgo);
        stats.setTotalIncome(currentIncome);
        stats.setTotalIncomeGrowth(calculateGrowthRate(previousIncome, currentIncome));

        // 计算任务完成率和变化
        Double currentCompletionRate = taskMapper.calculateCompletionRate(thirtyDaysAgo, now);
        Double previousCompletionRate = taskMapper.calculateCompletionRate(sixtyDaysAgo, thirtyDaysAgo);
        stats.setTaskCompletionRate(currentCompletionRate);
        // 完成率变化直接用差值，不需要计算增长率
        stats.setCompletionRateChange(currentCompletionRate - previousCompletionRate);

        return stats;
    }

    @Override
    public List<TaskTypeStatDTO> getTaskTypeStats() {
        return taskMapper.getTaskTypeStats();
    }

    /**
     * 计算增长率
     * 增长率的计算规则：
     * 1. 如果上期值为0，本期值也为0，则增长率为0
     * 2. 如果上期值为0，本期值大于0，则增长率为100%
     * 3. 如果上期值大于0，则使用正常的增长率计算公式
     *
     * @param previous 前一个时期的数值
     * @param current  当前时期的数值
     * @return 增长率（百分比）
     */
    private Double calculateGrowthRate(Number previous, Number current) {
        double previousValue = previous == null ? 0.0 : previous.doubleValue();
        double currentValue = current == null ? 0.0 : current.doubleValue();

        // 如果两个值都是0，增长率为0
        if (previousValue == 0 && currentValue == 0) {
            return 0.0;
        }
        
        // 如果上期是0，本期大于0，则增长率为100%
        if (previousValue == 0) {
            return 100.0;
        }
        
        // 正常计算增长率
        return (currentValue - previousValue) / previousValue * 100;
    }
} 