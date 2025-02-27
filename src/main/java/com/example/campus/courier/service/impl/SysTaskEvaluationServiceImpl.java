package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysTaskEvaluationHandler;
import com.example.campus.courier.service.SysTaskEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 任务评价服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysTaskEvaluationServiceImpl implements SysTaskEvaluationService {

    private final SysTaskEvaluationHandler sysTaskEvaluationHandler;

} 