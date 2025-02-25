package com.example.campus.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.entity.Task;
import com.example.campus.club.model.vo.TaskDetailVO;
import com.example.campus.club.model.vo.TaskVO;
import com.example.campus.club.model.vo.TaskMessageRequest;
import com.example.campus.club.model.entity.TaskMessage;

public interface TaskService {
    
    Page<Task> search(String keyword, Integer status, Long typeId, Integer pageNum, Integer pageSize);
    
    TaskDetailVO getDetail(Long id);
    
    // 获取抢单大厅任务列表
    Page<TaskVO> getTaskHallList(String keyword, Long typeId, Integer pageNum, Integer pageSize);
    
    // 获取任务详情
    TaskVO getTaskDetail(Long taskId);
    
    // 接单
    void takeTask(Long taskId, Long courierId);
    
    // 获取当前进行中的任务
    Page<TaskVO> getCurrentTasks(Long courierId, Integer pageNum, Integer pageSize);
    
    // 获取历史接单列表
    Page<TaskVO> getHistoryTasks(Long courierId, Integer pageNum, Integer pageSize);
    
    // 发送任务留言
    void sendMessage(TaskMessageRequest request, Long senderId);
    
    // 获取任务留言列表
    Page<TaskMessage> getTaskMessages(Long taskId, Integer pageNum, Integer pageSize);
} 