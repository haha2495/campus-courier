package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysDisputeSupportMapper;
import com.example.campus.courier.model.entity.SysDisputeSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysDisputeSupportHandler extends ServiceImpl<SysDisputeSupportMapper, SysDisputeSupport> {
    
    /**
     * 获取用户对指定纠纷的支持信息
     *
     * @param disputeId 纠纷ID
     * @param studentId 学生ID
     * @return 支持信息，如果不存在则返回null
     */
    public SysDisputeSupport getUserSupport(Integer disputeId, Integer studentId) {
        return this.getOne(
            new LambdaQueryWrapper<SysDisputeSupport>()
                .eq(SysDisputeSupport::getDisputeId, disputeId)
                .eq(SysDisputeSupport::getStudentId, studentId)
        );
    }

    /**
     * 获取指定纠纷的所有支持信息列表
     *
     * @param disputeId 纠纷ID
     * @return 支持信息列表
     */
    public List<SysDisputeSupport> getDisputeSupports(Integer disputeId) {
        return this.list(
            new LambdaQueryWrapper<SysDisputeSupport>()
                .eq(SysDisputeSupport::getDisputeId, disputeId)
                .orderByDesc(SysDisputeSupport::getCreateTime)
        );
    }
} 