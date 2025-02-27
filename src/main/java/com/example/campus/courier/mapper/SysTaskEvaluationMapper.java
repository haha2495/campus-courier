package com.example.campus.courier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 任务评价Mapper接口
 */
@Mapper
public interface SysTaskEvaluationMapper extends BaseMapper<SysTaskEvaluation> {

    /**
     * 获取跑腿者的平均评分
     *
     * @param runnerId 跑腿者ID
     * @return 平均评分（保留一位小数）
     */
    @Select("SELECT ROUND(AVG(score), 1) FROM sys_task_evaluation WHERE runner_id = #{runnerId}")
    Double getRunnerAverageScore(Integer runnerId);
} 