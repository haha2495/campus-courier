package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.mapper.handler.SysTaskLogHandler;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskLog;
import com.example.campus.courier.model.enums.TaskActionEnum;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.service.SysTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysTaskServiceImpl implements SysTaskService {

    private final SysTaskHandler sysTaskHandler;
    private final SysTaskLogHandler sysTaskLogHandler;

    /**
     * 保存任务
     *
     * @param request     任务添加请求
     * @param publisherId 发布者ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTask(TaskAddRequest request, Long publisherId) {
        // 1. 使用贫血模型初始化任务实体
        SysTask task = new SysTask().init(request, publisherId);

        // 2. 保存任务
        sysTaskHandler.save(task);

        // 3. 初始化并保存任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.PUBLISH, "发布任务");
        sysTaskLogHandler.save(taskLog);

        return true;
    }
} 