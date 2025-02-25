package com.example.campus.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.entity.DisputeType;
import com.example.campus.club.model.request.DisputeTypeRequest;

public interface DisputeTypeService {
    
    DisputeType add(DisputeTypeRequest request);
    
    void update(Long id, DisputeTypeRequest request);
    
    Page<DisputeType> search(String keyword, Integer pageNum, Integer pageSize);
} 