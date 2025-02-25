package com.example.campus.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.mapper.DisputeTypeMapper;
import com.example.campus.club.model.entity.DisputeType;
import com.example.campus.club.model.request.DisputeTypeRequest;
import com.example.campus.club.service.DisputeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisputeTypeServiceImpl implements DisputeTypeService {

    private final DisputeTypeMapper disputeTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DisputeType add(DisputeTypeRequest request) {
        DisputeType disputeType = new DisputeType();
        disputeType.setName(request.getName());
        disputeType.setDescription(request.getDescription());
        disputeType.setProcessingTime(request.getProcessingTime());
        disputeType.setCreateTime(LocalDateTime.now());
        disputeType.setUpdateTime(LocalDateTime.now());
        
        disputeTypeMapper.insert(disputeType);
        return disputeType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, DisputeTypeRequest request) {
        DisputeType disputeType = disputeTypeMapper.selectById(id);
        if (disputeType == null) {
            throw new RuntimeException("纠纷类型不存在");
        }
        
        disputeType.setName(request.getName());
        disputeType.setDescription(request.getDescription());
        disputeType.setProcessingTime(request.getProcessingTime());
        disputeType.setUpdateTime(LocalDateTime.now());
        
        disputeTypeMapper.updateById(disputeType);
    }

    @Override
    public Page<DisputeType> search(String keyword, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<DisputeType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(DisputeType::getName, keyword)
               .or()
               .like(DisputeType::getDescription, keyword)
               .orderByDesc(DisputeType::getCreateTime);
               
        return disputeTypeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
} 