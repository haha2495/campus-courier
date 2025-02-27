package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysStudentHandler;
import com.example.campus.courier.mapper.handler.SysTaskEvaluationHandler;
import com.example.campus.courier.mapper.handler.SysTaskHandler;
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

import java.time.LocalDateTime;

/**
 * 任务评价服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysTaskEvaluationServiceImpl implements SysTaskEvaluationService {

    private final SysTaskEvaluationHandler sysTaskEvaluationHandler;
    private final SysTaskHandler sysTaskHandler;
    private final SysStudentHandler sysStudentHandler;

    /**
     * 添加任务评价
     *
     * @param request 评价请求
     * @param userId  当前用户ID（评价人）
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEvaluation(EvaluationAddRequest request, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(request.getTaskId());
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (TaskStatusEnum.COMPLETED.getCode() != task.getStatus()) {
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
        return sysTaskEvaluationHandler.save(evaluation);
    }


} 