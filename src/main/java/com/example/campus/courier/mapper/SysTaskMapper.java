package com.example.campus.courier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.courier.model.dto.TaskTypeStatDTO;
import com.example.campus.courier.model.entity.SysTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 统计各任务类型的数量和占比
     */
    @Select("WITH TaskStats AS (" +
            "  SELECT " +
            "    t.task_type_id as typeId, " +
            "    tt.type_name as typeName, " +
            "    tt.icon as icon, " +
            "    COUNT(*) as taskCount, " +
            "    COUNT(*) * 100.0 / (SELECT COUNT(*) FROM sys_task) as percentage " +
            "  FROM sys_task t " +
            "  JOIN sys_task_types tt ON t.task_type_id = tt.id " +
            "  GROUP BY t.task_type_id, tt.type_name, tt.icon" +
            ") " +
            "SELECT * FROM TaskStats ORDER BY taskCount DESC")
    List<TaskTypeStatDTO> getTaskTypeStats();

    /**
     * 统计指定日期的已完成任务数
     */
    @Select("SELECT COUNT(*) FROM sys_task " +
            "WHERE status = 4 " +
            "AND DATE(create_time) = DATE(#{date})")
    Integer countCompletedTasksByDate(@Param("date") LocalDateTime date);

    /**
     * 统计指定日期的进行中任务数（已接单、进行中、待确认状态）
     */
    @Select("SELECT COUNT(*) FROM sys_task " +
            "WHERE status IN (1, 2, 3) " +
            "AND DATE(create_time) = DATE(#{date})")
    Integer countProcessingTasksByDate(@Param("date") LocalDateTime date);

    /**
     * 统计指定日期的待接单任务数
     */
    @Select("SELECT COUNT(*) FROM sys_task " +
            "WHERE status = 0 " +
            "AND DATE(create_time) = DATE(#{date})")
    Integer countPendingTasksByDate(@Param("date") LocalDateTime date);
} 