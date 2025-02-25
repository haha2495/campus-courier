package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskTypeMapper;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.request.task.TaskTypePageRequest;
import com.example.campus.courier.model.response.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 任务类型数据处理层
 */
@Slf4j
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
    
    /**
     * 启用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    public boolean enableTaskType(Integer id) {
        log.info("Handler - 启用任务类型, ID: {}", id);
        
        // 构建更新条件
        LambdaUpdateWrapper<SysTaskType> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysTaskType::getId, id)
                    .set(SysTaskType::getStatus, 1);
        
        // 执行更新操作
        return update(updateWrapper);
    }
    
    /**
     * 禁用任务类型
     *
     * @param id 任务类型ID
     * @return 操作是否成功
     */
    public boolean disableTaskType(Integer id) {
        log.info("Handler - 禁用任务类型, ID: {}", id);
        
        // 构建更新条件
        LambdaUpdateWrapper<SysTaskType> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysTaskType::getId, id)
                    .set(SysTaskType::getStatus, 0);
        
        // 执行更新操作
        return update(updateWrapper);
    }
    
    /**
     * 获取任务类型详情
     *
     * @param id 任务类型ID
     * @return 任务类型详情
     */
    public SysTaskType getTaskTypeDetail(Integer id) {
        log.info("Handler - 获取任务类型详情, ID: {}", id);
        
        // 直接通过ID查询
        return getById(id);
    }
}