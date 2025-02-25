package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysTaskTypeHandler;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
import com.example.campus.courier.service.SysTaskTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务类型服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTaskTypeServiceImpl implements SysTaskTypeService {

    private final SysTaskTypeHandler taskTypeHandler;

    /**
     * 添加任务类型
     *
     * @param request 任务类型添加请求
     * @return 添加后的任务类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTaskType(TaskTypeAddRequest request) {
        log.info("添加任务类型: {}", request);

        // 创建任务类型实体
        SysTaskType taskType = new SysTaskType().init(request);

        // 调用Handler保存任务类型
        return taskTypeHandler.save(taskType);
    }
} 