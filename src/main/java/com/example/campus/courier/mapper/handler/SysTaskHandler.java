package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskMapper;
import com.example.campus.courier.model.entity.SysTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskHandler extends ServiceImpl<SysTaskMapper, SysTask> {
    private final SysTaskMapper sysTaskMapper;
} 