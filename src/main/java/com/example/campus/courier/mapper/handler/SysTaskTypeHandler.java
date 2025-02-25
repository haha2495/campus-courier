package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskTypeMapper;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypePageRequest;
import com.example.campus.courier.model.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 任务类型数据处理层
 */
@Component
@RequiredArgsConstructor
public class SysTaskTypeHandler extends ServiceImpl<SysTaskTypeMapper, SysTaskType> {

    /**
     * 分页查询任务类型
     *
     * @param request 分页查询请求
     * @return 分页结果
     */
    public PageResult<SysTaskType> pageTaskTypes(TaskTypePageRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<SysTaskType> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果传入了状态参数，则按状态查询
        if (request.getStatus() != null) {
            queryWrapper.eq(SysTaskType::getStatus, request.getStatus());
        }
        
        // 按优先级降序、创建时间降序排序
        queryWrapper.orderByDesc(SysTaskType::getPriority)
                   .orderByDesc(SysTaskType::getCreateTime);
        
        // 执行分页查询
        Page<SysTaskType> page = page(
                new Page<>(request.getPageNum(), request.getPageSize()),
                queryWrapper
        );
        
        // 转换为PageResult
        return PageResult.build(page.getTotal(), page.getRecords());
    }
}