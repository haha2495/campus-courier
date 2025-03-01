package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.evaluation.EvaluationListDTO;
import com.example.campus.courier.model.entity.SysTaskEvaluation;
import com.example.campus.courier.model.request.evaluation.EvaluationAddRequest;

/**
 * 任务评价服务接口
 */
public interface SysTaskEvaluationService {
    
    /**
     * 添加任务评价
     *
     * @param request    评价请求
     * @param publisherId 评价人ID
     * @return 操作结果
     */
    Boolean addEvaluation(EvaluationAddRequest request, Integer publisherId);

    /**
     * 查询学生收到的评价列表
     *
     * @param studentId 学生ID
     * @return 评价列表数据
     */
    EvaluationListDTO getReceivedEvaluations(Integer studentId);
} 