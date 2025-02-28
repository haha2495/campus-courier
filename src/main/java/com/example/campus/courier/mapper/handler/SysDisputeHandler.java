package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysDisputeMapper;
import com.example.campus.courier.model.entity.SysDispute;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import org.springframework.stereotype.Component;

/**
 * 纠纷数据处理器
 */
@Component
public class SysDisputeHandler extends ServiceImpl<SysDisputeMapper, SysDispute> {

    /**
     * 分页查询纠纷
     *
     * @param request 分页查询参数
     * @return 分页结果
     */
    public Page<SysDispute> pageDisputes(DisputePageRequest request) {
        // 1. 创建分页对象
        Page<SysDispute> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 2. 构建纠纷查询条件
        LambdaQueryWrapper<SysDispute> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getStatus() != null) {
            queryWrapper.eq(SysDispute::getStatus, request.getStatus());
        }
        queryWrapper.orderByDesc(SysDispute::getCreateTime);

        // 3. 执行分页查询
        return this.page(page, queryWrapper);
    }
} 