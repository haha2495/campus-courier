package com.example.campus.courier.service;

import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.request.task.TaskAddRequest;

/**
 * 任务服务接口
 */
public interface SysTaskService {
    // 在这里定义任务相关的业务逻辑方法
    
    /**
     * 保存任务
     * @param request 任务添加请求
     * @param publisherId 发布者ID
     * @return 操作是否成功
     */
    boolean saveTask(TaskAddRequest request, Long publisherId);
} 