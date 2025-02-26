package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskMapper;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务数据处理层
 */
@Component
@RequiredArgsConstructor
public class SysTaskHandler extends ServiceImpl<SysTaskMapper, SysTask> {
    
    /**
     * 查询用户发布的任务列表
     *
     * @param request 查询参数
     * @param publisherId 发布者ID
     * @return 分页结果
     */
    public Page<SysTask> getPublishedTasks(TaskQueryRequest request, Integer publisherId) {
        // 创建分页对象
        Page<SysTask> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getPublisherId, publisherId); // 查询当前用户发布的任务
        
        // 如果指定了状态，则按状态筛选
        if (request.getStatus() != null) {
            queryWrapper.eq(SysTask::getStatus, request.getStatus());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(SysTask::getCreateTime);
        
        // 执行分页查询并返回结果
        return page(page, queryWrapper);
    }

    /**
     * 获取抢单大厅任务列表
     * 查询所有待接单的任务，按发布时间倒序排序
     *
     * @return 任务列表
     */
    public List<SysTask> getTaskHallList() {
        return lambdaQuery()
                .eq(SysTask::getStatus, TaskStatusEnum.PENDING.getCode())
                .orderByDesc(SysTask::getCreateTime)
                .list();
    }
} 