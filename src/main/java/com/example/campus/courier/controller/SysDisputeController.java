package com.example.campus.courier.controller;

import com.example.campus.courier.service.SysDisputeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 纠纷控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispute")
@Tag(name = "纠纷管理接口")
public class SysDisputeController {
    
    private final SysDisputeService disputeService;
} 