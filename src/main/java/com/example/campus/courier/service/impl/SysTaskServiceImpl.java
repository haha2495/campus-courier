package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.mapper.handler.SysTaskLogHandler;
import com.example.campus.courier.service.SysTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysTaskServiceImpl implements SysTaskService {

    private final SysTaskHandler sysTaskHandler;
    private final SysTaskLogHandler sysTaskLogHandler;

    // 在这里实现任务相关的业务逻辑方法
} 