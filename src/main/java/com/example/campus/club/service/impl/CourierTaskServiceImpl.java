package com.example.campus.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.mapper.CourierTaskMapper;
import com.example.campus.club.mapper.TaskMessageMapper;
import com.example.campus.club.model.entity.CourierTask;
import com.example.campus.club.model.entity.TaskMessage;
import com.example.campus.club.model.vo.CourierTaskVO;
import com.example.campus.club.model.request.TaskMessageRequest;
import com.example.campus.club.service.CourierTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourierTaskServiceImpl implements CourierTaskService {

    private final CourierTaskMapper courierTaskMapper;
    private final TaskMessageMapper taskMessageMapper;

    @Override
    public Page<CourierTaskVO> getTaskHallList(String keyword, Long typeId, Integer pageNum, Integer pageSize) {
        return courierTaskMapper.getTaskHallList(keyword, typeId, new Page<>(pageNum, pageSize));
    }

    @Override
    public CourierTaskVO getTaskDetail(Long taskId) {
        return courierTaskMapper.getTaskDetail(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeTask(Long taskId, Long courierId) {
        CourierTask task = courierTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        if (task.getStatus() != 0) {
            throw new RuntimeException("任务已被接单");
        }
        
        task.setCourierId(courierId);
        task.setStatus(1);
        task.setUpdateTime(LocalDateTime.now());
        
        courierTaskMapper.updateById(task);
    }

    @Override
    public Page<CourierTaskVO> getCurrentTasks(Long courierId, Integer pageNum, Integer pageSize) {
        return courierTaskMapper.getCurrentTasks(courierId, new Page<>(pageNum, pageSize));
    }

    @Override
    public Page<CourierTaskVO> getHistoryTasks(Long courierId, Integer pageNum, Integer pageSize) {
        return courierTaskMapper.getHistoryTasks(courierId, new Page<>(pageNum, pageSize));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(TaskMessageRequest request, Long senderId) {
        TaskMessage message = new TaskMessage();
        message.setTaskId(request.getTaskId());
        message.setSenderId(senderId);
        message.setContent(request.getContent());
        message.setCreateTime(LocalDateTime.now());
        
        taskMessageMapper.insert(message);
    }

    @Override
    public Page<TaskMessage> getTaskMessages(Long taskId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<TaskMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskMessage::getTaskId, taskId)
               .orderByDesc(TaskMessage::getCreateTime);
               
        return taskMessageMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
} 