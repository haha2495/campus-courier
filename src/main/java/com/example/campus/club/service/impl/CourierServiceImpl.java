package com.example.campus.club.service.impl;

import com.example.campus.club.mapper.CourierMapper;
import com.example.campus.club.model.entity.Courier;
import com.example.campus.club.model.request.CourierProfileRequest;
import com.example.campus.club.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierMapper courierMapper;

    @Override
    public Courier getCourierInfo(Long userId) {
        return courierMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, CourierProfileRequest request) {
        Courier courier = courierMapper.selectByUserId(userId);
        if (courier == null) {
            throw new RuntimeException("跑腿者不存在");
        }
        
        courier.setAvatar(request.getAvatar());
        courier.setNickname(request.getNickname());
        courier.setContact(request.getContact());
        courier.setIntroduction(request.getIntroduction());
        courier.setUpdateTime(LocalDateTime.now());
        
        courierMapper.updateById(courier);
    }
} 