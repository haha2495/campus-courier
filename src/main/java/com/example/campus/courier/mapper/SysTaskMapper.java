package com.example.campus.courier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.courier.model.entity.SysTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 任务Mapper接口
 */
public interface SysTaskMapper extends BaseMapper<SysTask> {

    /**
     * 统计指定时间范围内的总任务数
     */
    @Select("SELECT COUNT(*) FROM sys_task WHERE create_time BETWEEN #{startTime} AND #{endTime}")
    Integer countTotalTasks(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的总收入
     * 总收入 = 基础报酬 + 加急费（如果是加急单）
     */
    @Select("SELECT COALESCE(SUM(CASE " +
            "WHEN urgent = 1 THEN reward + COALESCE(urgent_reward, 0) " +
            "ELSE reward " +
            "END), 0) " +
            "FROM sys_task " +
            "WHERE status = 4 AND create_time BETWEEN #{startTime} AND #{endTime}")
    Double sumTotalIncome(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 计算指定时间范围内的任务完成率
     */
    @Select("SELECT COALESCE(CAST(SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS DECIMAL) / COUNT(*) * 100, 0) " +
            "FROM sys_task WHERE create_time BETWEEN #{startTime} AND #{endTime}")
    Double calculateCompletionRate(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
} 