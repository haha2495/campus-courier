package com.example.campus.courier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.campus.courier.mapper.handler.SysStudentHandler;
import com.example.campus.courier.mapper.handler.SysTaskEvaluationHandler;
import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.model.dto.evaluation.EvaluationListDTO;
import com.example.campus.courier.model.dto.evaluation.EvaluationStatisticsDTO;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.evaluation.EvaluationAddRequest;
import com.example.campus.courier.service.SysTaskEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 任务评价服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysTaskEvaluationServiceImpl implements SysTaskEvaluationService {

    private final SysTaskEvaluationHandler sysTaskEvaluationHandler;
    private final SysTaskHandler sysTaskHandler;

    /**
     * 添加任务评价
     *
     * @param request 评价请求
     * @param userId  当前用户ID（评价人）
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addEvaluation(EvaluationAddRequest request, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(request.getTaskId());
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (!Objects.equals(TaskStatusEnum.COMPLETED.getCode(), task.getStatus())) {
            throw new IllegalArgumentException("只有已完成的任务才能评价");
        }

        // 3. 校验是否是发布者
        if (!userId.equals(task.getPublisherId())) {
            throw new IllegalArgumentException("只有任务发布者才能评价");
        }

        // 4. 校验是否已评价
        boolean exists = sysTaskEvaluationHandler.existsByTaskId(request.getTaskId());
        if (exists) {
            throw new IllegalArgumentException("该任务已评价");
        }

        // 5. 使用贫血模型初始化评价记录
        SysTaskEvaluation evaluation = new SysTaskEvaluation()
                .init(request, userId, task.getRunnerId());

        // 6. 保存评价
        boolean saved = sysTaskEvaluationHandler.save(evaluation);
        
        if (saved) {
            // 7. 更新任务状态为已评价
            task.setStatus(TaskStatusEnum.EVALUATED.getCode());
            sysTaskHandler.updateById(task);
        }

        return saved;
    }

    @Override
    public EvaluationListDTO getReceivedEvaluations(Integer studentId) {
        // 1. 查询评价列表
        List<SysTaskEvaluation> evaluations = sysTaskEvaluationHandler.lambdaQuery()
                .eq(SysTaskEvaluation::getRunnerId, studentId)
                .orderByDesc(SysTaskEvaluation::getCreateTime)
                .list();

        // 2. 计算统计数据
        EvaluationStatisticsDTO statistics = new EvaluationStatisticsDTO();
        if (!evaluations.isEmpty()) {
            // 2.1 计算总数
            statistics.setTotalCount((long) evaluations.size());

            // 2.2 计算平均分
            double averageScore = evaluations.stream()
                    .mapToDouble(e -> e.getScore().doubleValue())
                    .average()
                    .orElse(0.0);
            statistics.setAverageScore(BigDecimal.valueOf(averageScore).setScale(1, RoundingMode.HALF_UP));

            // 2.3 计算好评率 (4分以上算好评)
            long positiveCount = evaluations.stream()
                    .filter(e -> e.getScore().compareTo(BigDecimal.valueOf(4.0)) >= 0)
                    .count();
            BigDecimal positiveRate = BigDecimal.valueOf(positiveCount)
                    .divide(BigDecimal.valueOf(evaluations.size()), 2, RoundingMode.HALF_UP);
            statistics.setPositiveRate(positiveRate);
        } else {
            statistics.setTotalCount(0L)
                    .setAverageScore(BigDecimal.ZERO)
                    .setPositiveRate(BigDecimal.ZERO);
        }

        // 3. 组装返回数据
        return new EvaluationListDTO()
                .setStatistics(statistics)
                .setEvaluations(evaluations);
    }
} 