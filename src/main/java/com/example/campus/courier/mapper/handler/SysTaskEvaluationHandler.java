package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskEvaluationMapper;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务评价处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskEvaluationHandler extends ServiceImpl<SysTaskEvaluationMapper, SysTaskEvaluation> {

    private final SysTaskEvaluationMapper sysTaskEvaluationMapper;

    /**
     * 检查任务是否已评价
     *
     * @param taskId 任务ID
     * @return true: 已评价, false: 未评价
     */
    public boolean existsByTaskId(Integer taskId) {
        return count(new LambdaQueryWrapper<SysTaskEvaluation>()
                .eq(SysTaskEvaluation::getTaskId, taskId)) > 0;
    }
} 