package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysTaskTypeHandler;
import com.example.campus.courier.service.SysTaskTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 任务类型服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTaskTypeServiceImpl implements SysTaskTypeService {

    private final SysTaskTypeHandler taskTypeHandler;

} 