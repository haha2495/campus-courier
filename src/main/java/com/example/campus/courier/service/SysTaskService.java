package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskMessageAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.model.dto.TaskDetailDTO;
import com.example.campus.courier.model.dto.RunnerTaskStatDTO;

import java.util.List;
import java.util.Map;

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
    boolean startTask(Integer taskId, Integer userId);

    /**
     * 获取任务详情
     * 包含任务信息、任务类型信息和留言列表
     *
     * @param taskId 任务ID
     * @param loginUserId 当前登录用户ID
     * @return 任务详情DTO
     */
    TaskDetailDTO getTaskDetail(Integer taskId, Integer loginUserId);

    /**
     * 添加任务留言
     *
     * @param request 留言请求
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    boolean addTaskMessage(TaskMessageAddRequest request, Integer userId);

    /**
     * 送达任务
     * 跑腿者标记任务已送达，等待发布者确认
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    boolean deliverTask(Integer taskId, Integer userId);

    /**
     * 确认任务完成
     * 发布者确认跑腿者已完成任务，任务状态变更为已完成
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID（发布者）
     * @return 操作是否成功
     */
    boolean confirmTask(Integer taskId, Integer userId);

    /**
     * 管理员分页查询任务列表
     * 
     * @param request 查询参数
     * @return 任务列表分页结果
     */
    PageResult<TaskDTO> pageTask(TaskQueryRequest request);

    /**
     * 获取跑腿者已完成的任务列表及统计信息
     *
     * @param userId 用户ID
     * @return 任务列表及统计信息
     */
    RunnerTaskStatDTO getMyCompletedTasks(Integer userId);

    /**
     * 获取发布者任务统计数据
     * 包含进行中、已完成、待支付的任务数量
     *
     * @param userId 用户ID
     * @return 任务统计数据，key为状态（processing-进行中, completed-已完成, pending_payment-待支付），value为数量
     */
    Map<String, Integer> getPublisherTaskStats(Integer userId);

    /**
     * 获取跑腿者任务统计数据
     * 包含进行中、已完成、总收入的统计
     *
     * @param userId 用户ID
     * @return 任务统计数据，key为状态（processing-进行中, completed-已完成, total_income-总收入），value为数量或金额
     */
    Map<String, Object> getRunnerTaskStats(Integer userId);
} 