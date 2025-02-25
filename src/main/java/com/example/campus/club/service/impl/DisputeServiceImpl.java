package com.example.campus.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.mapper.DisputeMapper;
import com.example.campus.club.model.entity.Dispute;
import com.example.campus.club.model.vo.DisputeDetailVO;
import com.example.campus.club.model.request.DisputeApprovalRequest;
import com.example.campus.club.service.DisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisputeServiceImpl implements DisputeService {

    private final DisputeMapper disputeMapper;

    @Override
    public Page<Dispute> search(String keyword, Integer status, Long typeId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Dispute> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Dispute::getTitle, keyword)
                  .or()
                  .like(Dispute::getDescription, keyword);
        }
        
        if (status != null) {
            wrapper.eq(Dispute::getStatus, status);
        }
        
        if (typeId != null) {
            wrapper.eq(Dispute::getTypeId, typeId);
        }
        
        wrapper.orderByDesc(Dispute::getCreateTime);
        
        return disputeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public DisputeDetailVO getDetail(Long id) {
        return disputeMapper.getDisputeDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(DisputeApprovalRequest request) {
        Dispute dispute = disputeMapper.selectById(request.getDisputeId());
        if (dispute == null) {
            throw new RuntimeException("纠纷不存在");
        }
        
        dispute.setStatus(2); // 设置为已完成
        dispute.setResult(request.getResult());
        dispute.setHandlerId(getCurrentUserId()); // 从BaseController获取当前用户ID
        dispute.setUpdateTime(LocalDateTime.now());
        
        disputeMapper.updateById(dispute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forward(Long id) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute == null) {
            throw new RuntimeException("纠纷不存在");
        }
        
        dispute.setStatus(3); // 设置为已转发
        dispute.setUpdateTime(LocalDateTime.now());
        
        disputeMapper.updateById(dispute);
    }
} 