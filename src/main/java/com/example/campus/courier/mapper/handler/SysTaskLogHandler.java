package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskLogMapper;
import com.example.campus.courier.model.entity.SysTaskLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务日志处理类
 */
@Service
@RequiredArgsConstructor
public class SysTaskLogHandler extends ServiceImpl<SysTaskLogMapper, SysTaskLog> {

    private final SysTaskLogMapper sysTaskLogMapper;

    /**
     * 获取指定任务的日志列表
     * 按时间倒序排序
     *
     * @param taskId 任务ID
     * @return 日志列表
     */
    public List<SysTaskLog> getTaskLogs(Integer taskId) {
        return list(new LambdaQueryWrapper<SysTaskLog>()
                .eq(SysTaskLog::getTaskId, taskId)
                .orderByDesc(SysTaskLog::getActionTime));
    }
}