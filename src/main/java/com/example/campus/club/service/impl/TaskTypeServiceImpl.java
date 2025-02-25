package com.example.campus.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.mapper.TaskTypeMapper;
import com.example.campus.club.model.entity.TaskType;
import com.example.campus.club.model.request.TaskTypeRequest;
import com.example.campus.club.service.TaskTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private final TaskTypeMapper taskTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskType add(TaskTypeRequest request) {
        TaskType taskType = new TaskType();
        taskType.setName(request.getName());
        taskType.setDescription(request.getDescription());
        taskType.setCreateTime(LocalDateTime.now());
        taskType.setUpdateTime(LocalDateTime.now());
        
        taskTypeMapper.insert(taskType);
        return taskType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, TaskTypeRequest request) {
        TaskType taskType = taskTypeMapper.selectById(id);
        if (taskType == null) {
            throw new RuntimeException("任务类型不存在");
        }
        
        taskType.setName(request.getName());
        taskType.setDescription(request.getDescription());
        taskType.setUpdateTime(LocalDateTime.now());
        
        taskTypeMapper.updateById(taskType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TaskType taskType = taskTypeMapper.selectById(id);
        if (taskType == null) {
            throw new RuntimeException("任务类型不存在");
        }
        
        taskTypeMapper.deleteById(id);
    }

    @Override
    public Page<TaskType> search(String keyword, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<TaskType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(TaskType::getName, keyword)
               .or()
               .like(TaskType::getDescription, keyword)
               .orderByDesc(TaskType::getCreateTime);
               
        return taskTypeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
} 