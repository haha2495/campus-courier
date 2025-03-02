package com.example.campus.courier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.courier.model.entity.SysStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface SysStudentMapper extends BaseMapper<SysStudent> {

    /**
     * 统计指定时间范围内的活跃用户数
     * 活跃用户定义：在指定时间范围内有发布任务或接单的用户
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 活跃用户数
     */
    @Select("SELECT COUNT(DISTINCT u.id) FROM sys_student u " +
            "LEFT JOIN sys_task t1 ON u.id = t1.publisher_id AND t1.create_time BETWEEN #{startTime} AND #{endTime} " +
            "LEFT JOIN sys_task t2 ON u.id = t2.runner_id AND t2.create_time BETWEEN #{startTime} AND #{endTime} " +
            "WHERE t1.id IS NOT NULL OR t2.id IS NOT NULL")
    Integer countActiveUsers(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
} 