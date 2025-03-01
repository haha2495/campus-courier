package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskEvaluationMapper;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务评价数据处理层
 */
@Slf4j
@Component
public class SysTaskEvaluationHandler extends ServiceImpl<SysTaskEvaluationMapper, SysTaskEvaluation> {

    /**
     * 检查任务是否已评价
     *
     * @param taskId 任务ID
     * @return 是否已评价
     */
    public boolean existsByTaskId(Integer taskId) {
        return lambdaQuery()
                .eq(SysTaskEvaluation::getTaskId, taskId)
                .exists();
    }

    /**
     * 查询学生收到的评价列表
     *
     * @param studentId 学生ID
     * @return 评价列表
     */
    public List<SysTaskEvaluation> getReceivedEvaluations(Integer studentId) {
        return lambdaQuery()
                .eq(SysTaskEvaluation::getRunnerId, studentId)
                .orderByDesc(SysTaskEvaluation::getCreateTime)
                .list();
    }
}