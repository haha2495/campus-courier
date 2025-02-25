package com.example.campus.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.mapper.TaskMapper;
import com.example.campus.club.mapper.TaskMessageMapper;
import com.example.campus.club.model.entity.Task;
import com.example.campus.club.model.vo.TaskDetailVO;
import com.example.campus.club.model.vo.TaskVO;
import com.example.campus.club.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskMessageMapper taskMessageMapper;

    @Override
    public Page<Task> search(String keyword, Integer status, Long typeId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Task::getTitle, keyword)
                  .or()
                  .like(Task::getDescription, keyword);
        }
        
        // 状态筛选
        if (status != null) {
            wrapper.eq(Task::getStatus, status);
        }
        
        // 类型筛选
        if (typeId != null) {
            wrapper.eq(Task::getTypeId, typeId);
        }
        
        wrapper.orderByDesc(Task::getCreateTime);
        
        return taskMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public TaskDetailVO getDetail(Long id) {
        return taskMapper.getTaskDetail(id);
    }

    @Override
    public Page<TaskVO> getTaskHallList(String keyword, Long typeId, Integer pageNum, Integer pageSize) {
        return taskMapper.getTaskHallList(keyword, typeId, new Page<>(pageNum, pageSize));
    }
} 