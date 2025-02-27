package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.response.PageResult;

import java.util.List;

/**
 * 任务服务接口
 */
public interface SysTaskService {
    // 在这里定义任务相关的业务逻辑方法
    
    /**
     * 保存任务
     * @param request 任务添加请求
     * @param publisherId 发布者ID
     * @return 操作是否成功
     */
    boolean saveTask(TaskAddRequest request, Integer publisherId);
    
    /**
     * 获取用户发布的任务列表
     * 
     * @param request 查询参数
     * @param publisherId 发布者ID
     * @return 任务列表分页结果
     */
    PageResult<TaskDTO> getPublishedTasks(TaskQueryRequest request, Integer publisherId);

    /**
     * 取消任务
     * 
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    boolean cancelTask(Long taskId, Integer userId);

    /**
     * 获取抢单大厅任务列表
     * 查询所有待接单的任务，按发布时间倒序排序
     *
     * @return 任务列表
     */
    List<TaskDTO> getTaskHallList(Integer loginUserId);

    /**
     * 抢单（接受任务）
     * 
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    boolean acceptTask(Long taskId, Integer userId);

    /**
     * 获取我当前接单的任务列表
     * 只返回进行中的任务（已接单、进行中、待确认状态）
     *
     * @param userId 当前用户ID
     * @return 任务列表
     */
    List<TaskDTO> getMyAcceptedTasks(Integer userId);

    /**
     * 开始执行任务
     * 将任务状态从"已接单"变更为"进行中"
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    boolean startTask(Long taskId, Integer userId);
} 