package com.example.campus.courier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
import com.example.campus.courier.model.request.task.TaskTypePageRequest;
import com.example.campus.courier.model.response.PageResult;

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
    
    /**
     * 分页查询任务类型
     *
     * @param request 分页查询请求
     * @return 任务类型分页数据
     */
    PageResult<SysTaskType> pageTaskTypes(TaskTypePageRequest request);
    
    /**
     * 启用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    boolean enableTaskType(Integer id);
    
    /**
     * 禁用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    boolean disableTaskType(Integer id);
} 