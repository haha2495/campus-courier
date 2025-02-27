package com.example.campus.courier.service;

import com.example.campus.courier.model.request.evaluation.EvaluationAddRequest;

/**
 * 任务评价服务接口
 */
public interface SysTaskEvaluationService {
    
    /**
     * 添加任务评价
     *
     * @param request 评价请求
     * @param userId 当前用户ID（评价人）
     * @return 操作是否成功
     */
    boolean addEvaluation(EvaluationAddRequest request, Integer userId);
} 