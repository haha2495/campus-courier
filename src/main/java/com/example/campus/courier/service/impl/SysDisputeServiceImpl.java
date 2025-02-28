package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysDisputeHandler;
import com.example.campus.courier.mapper.handler.SysDisputeSupportHandler;
import com.example.campus.courier.service.SysDisputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 纠纷服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDisputeServiceImpl implements SysDisputeService {
    
    private final SysDisputeHandler disputeHandler;
    private final SysDisputeSupportHandler disputeSupportHandler;
}