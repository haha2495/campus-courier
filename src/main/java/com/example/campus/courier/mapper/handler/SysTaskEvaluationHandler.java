package com.example.campus.courier.mapper.handler;

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

} 