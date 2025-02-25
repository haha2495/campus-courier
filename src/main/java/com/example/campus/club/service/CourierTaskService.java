package com.example.campus.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.vo.CourierTaskVO;
import com.example.campus.club.model.request.TaskMessageRequest;

public interface CourierTaskService {
    
    // 获取抢单大厅任务列表
    Page<CourierTaskVO> getTaskHallList(String keyword, Long typeId, Integer pageNum, Integer pageSize);
    
    // 获取任务详情
    CourierTaskVO getTaskDetail(Long taskId);
    
    // 接单
    void takeTask(Long taskId, Long courierId);
    
    // 获取当前进行中的任务
    Page<CourierTaskVO> getCurrentTasks(Long courierId, Integer pageNum, Integer pageSize);
    
    // 获取历史接单列表
    Page<CourierTaskVO> getHistoryTasks(Long courierId, Integer pageNum, Integer pageSize);
    
    // 发送任务留言
    void sendMessage(TaskMessageRequest request, Long senderId);
    
    // 获取任务留言列表
    Page<TaskMessage> getTaskMessages(Long taskId, Integer pageNum, Integer pageSize);
} 