package com.example.campus.courier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.mapper.handler.SysTaskTypeHandler;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypeAddRequest;
import com.example.campus.courier.model.request.task.TaskTypePageRequest;
import com.example.campus.courier.model.response.PageResult;
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

    /**
     * 分页查询任务类型
     *
     * @param request 分页查询请求
     * @return 任务类型分页数据
     */
    @Override
    public PageResult<SysTaskType> pageTaskTypes(TaskTypePageRequest request) {
        log.info("分页查询任务类型: {}", request);
        
        // 调用Handler执行分页查询
        return taskTypeHandler.pageTaskTypes(request);
    }
    
    /**
     * 启用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableTaskType(Integer id) {
        log.info("启用任务类型, ID: {}", id);
        
        // 调用Handler执行启用操作
        return taskTypeHandler.enableTaskType(id);
    }
    
    /**
     * 禁用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableTaskType(Integer id) {
        log.info("禁用任务类型, ID: {}", id);
        
        // 调用Handler执行禁用操作
        return taskTypeHandler.disableTaskType(id);
    }
    
    /**
     * 获取任务类型详情
     *
     * @param id 任务类型ID
     * @return 任务类型详情
     */
    @Override
    public SysTaskType getTaskTypeDetail(Integer id) {
        log.info("获取任务类型详情, ID: {}", id);
        
        // 调用Handler获取任务类型详情
        return taskTypeHandler.getTaskTypeDetail(id);
    }
} 