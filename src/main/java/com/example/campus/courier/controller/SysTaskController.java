package com.example.campus.courier.controller;

import com.example.campus.courier.mapper.handler.SysTaskHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class SysTaskController {

    private final SysTaskHandler sysTaskHandler;

} 