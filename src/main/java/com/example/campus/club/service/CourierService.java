package com.example.campus.club.service;

import com.example.campus.club.model.entity.Courier;
import com.example.campus.club.model.request.CourierProfileRequest;

public interface CourierService {
    
    // 获取跑腿者信息
    Courier getCourierInfo(Long userId);
    
    // 更新跑腿者信息
    void updateProfile(Long userId, CourierProfileRequest request);
} 