package com.example.campus.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.entity.Dispute;
import com.example.campus.club.model.vo.DisputeDetailVO;
import com.example.campus.club.model.request.DisputeApprovalRequest;

public interface DisputeService {
    
    Page<Dispute> search(String keyword, Integer status, Long typeId, Integer pageNum, Integer pageSize);
    
    DisputeDetailVO getDetail(Long id);
    
    void approve(DisputeApprovalRequest request);
    
    void forward(Long id);
} 