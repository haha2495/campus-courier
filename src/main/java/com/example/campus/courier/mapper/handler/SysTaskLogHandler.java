package com.example.campus.courier.mapper.handler;

import com.example.campus.courier.mapper.SysTaskLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务日志处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskLogHandler {

    private final SysTaskLogMapper sysTaskLogMapper;

}