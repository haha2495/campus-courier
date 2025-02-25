package com.example.campus.club.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.model.entity.CourierTask;
import com.example.campus.club.model.vo.CourierTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourierTaskMapper extends BaseMapper<CourierTask> {
    
    // 获取抢单大厅任务列表
    Page<CourierTaskVO> getTaskHallList(@Param("keyword") String keyword, 
                                       @Param("typeId") Long typeId,
                                       Page<CourierTaskVO> page);
    
    // 获取任务详情
    CourierTaskVO getTaskDetail(@Param("taskId") Long taskId);
    
    // 获取当前进行中的任务
    Page<CourierTaskVO> getCurrentTasks(@Param("courierId") Long courierId,
                                      Page<CourierTaskVO> page);
    
    // 获取历史接单列表
    Page<CourierTaskVO> getHistoryTasks(@Param("courierId") Long courierId,
                                      Page<CourierTaskVO> page);
} 