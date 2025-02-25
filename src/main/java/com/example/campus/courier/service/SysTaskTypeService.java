package com.example.campus.courier.service;

import com.example.campus.courier.model.request.task.TaskTypeAddRequest;

/**
 * 任务类型服务接口
 */
public interface SysTaskTypeService {

    /**
     * 添加任务类型
     *
     * @param request 任务类型添加请求
     * @return 添加后的任务类型
     */
    boolean addTaskType(TaskTypeAddRequest request);

} 