package com.example.campus.courier.mapper.handler;

import com.example.campus.courier.mapper.SysTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskHandler {
    private final SysTaskMapper sysTaskMapper;
} 