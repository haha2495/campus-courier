package com.example.campus.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.entity.TaskType;
import com.example.campus.club.model.request.TaskTypeRequest;

public interface TaskTypeService {
    
    TaskType add(TaskTypeRequest request);
    
    void update(Long id, TaskTypeRequest request);
    
    void delete(Long id);
    
    Page<TaskType> search(String keyword, Integer pageNum, Integer pageSize);
} 