package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskLogMapper;
import com.example.campus.courier.model.entity.SysTaskLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 任务日志处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskLogHandler extends ServiceImpl<SysTaskLogMapper, SysTaskLog> {

    private final SysTaskLogMapper sysTaskLogMapper;

}