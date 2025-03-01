package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysDisputeMapper;
import com.example.campus.courier.model.entity.SysDispute;
import com.example.campus.courier.model.enums.DisputeStatusEnum;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 根据任务ID获取纠纷信息
     *
     * @param taskId 任务ID
     * @return 纠纷信息
     */
    public SysDispute getByTaskId(Integer taskId) {
        return this.getOne(
            new LambdaQueryWrapper<SysDispute>()
                .eq(SysDispute::getTaskId, taskId)
        );
    }

    /**
     * 查询纠纷大厅数据
     * 获取当前在纠纷大厅展示的纠纷列表
     *
     * @return 纠纷列表
     */
    public List<SysDispute> listHallDisputes() {
        return this.list(
            new LambdaQueryWrapper<SysDispute>()
                .eq(SysDispute::getStatus, DisputeStatusEnum.PUBLISHED.getCode())
                .ge(SysDispute::getDeadline, LocalDateTime.now())
                .orderByDesc(SysDispute::getCreateTime)
        );
    }

    /**
     * 更新已过期纠纷状态为待处理
     *
     * @param disputeId 纠纷ID
     * @return 是否更新成功
     */
    public boolean updateExpiredDisputeStatus(Integer disputeId) {
        return this.lambdaUpdate()
            .eq(SysDispute::getId, disputeId)
            .eq(SysDispute::getStatus, DisputeStatusEnum.PUBLISHED.getCode())
            .set(SysDispute::getStatus, DisputeStatusEnum.PENDING.getCode())
            .update();
    }
} 