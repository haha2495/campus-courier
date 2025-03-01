package com.example.campus.courier.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.exception.BusinessException;
import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.mapper.handler.SysTaskLogHandler;
import com.example.campus.courier.mapper.handler.SysTaskTypeHandler;
import com.example.campus.courier.mapper.handler.SysTaskMessageHandler;
import com.example.campus.courier.mapper.handler.SysStudentHandler;
import com.example.campus.courier.mapper.handler.SysTaskEvaluationHandler;
import com.example.campus.courier.mapper.handler.SysDisputeHandler;
import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.dto.TaskDetailDTO;
import com.example.campus.courier.model.dto.TaskMessageDTO;
import com.example.campus.courier.model.entity.*;
import com.example.campus.courier.model.enums.TaskActionEnum;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.request.task.TaskMessageAddRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.service.SysTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysTaskServiceImpl implements SysTaskService {

    private final SysTaskHandler sysTaskHandler;
    private final SysTaskLogHandler sysTaskLogHandler;
    private final SysTaskTypeHandler sysTaskTypeHandler;
    private final SysTaskMessageHandler sysTaskMessageHandler;
    private final SysStudentHandler sysStudentHandler;
    private final SysTaskEvaluationHandler sysTaskEvaluationHandler;
    private final SysDisputeHandler sysDisputeHandler;

    /**
     * 保存任务
     *
     * @param request     任务添加请求
     * @param publisherId 发布者ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTask(TaskAddRequest request, Integer publisherId) {
        // 1. 获取发布者信息并校验余额
        SysStudent publisher = sysStudentHandler.getById(publisherId);
        BigDecimal totalPrice = request.getReward();
        if (request.getUrgent() && request.getUrgentReward() != null) {
            totalPrice = totalPrice.add(request.getUrgentReward());
        }
        
        if (!publisher.hasEnoughBalance(totalPrice)) {
            throw new BusinessException("账户余额不足,请先充值");
        }

        // 2. 使用贫血模型初始化任务实体
        SysTask task = new SysTask().init(request, publisherId);

        // 3. 保存任务
        sysTaskHandler.save(task);

        // 4. 扣减发布者余额
        publisher.decreaseBalance(totalPrice);
        sysStudentHandler.updateById(publisher);

        // 5. 初始化并保存任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.PUBLISH, "发布任务:" + task.getTitle(), publisherId);
        sysTaskLogHandler.save(taskLog);

        return true;
    }

    /**
     * 获取用户发布的任务列表
     *
     * @param request     查询参数
     * @param publisherId 发布者ID
     * @return 任务列表分页结果
     */
    @Override
    public PageResult<TaskDTO> getPublishedTasks(TaskQueryRequest request, Integer publisherId) {
        // 1. 使用Handler查询任务基础数据
        Page<SysTask> taskPage = sysTaskHandler.getPublishedTasks(request, publisherId);

        // 2. 如果没有数据，返回空结果
        if (taskPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }

        // 3. 提取任务类型ID列表
        List<Integer> typeIds = taskPage.getRecords().stream()
                .map(SysTask::getTaskTypeId)
                .distinct()
                .collect(Collectors.toList());

        // 4. 通过TaskTypeHandler查询任务类型数据
        List<SysTaskType> taskTypes = sysTaskTypeHandler.getTaskTypesByIds(typeIds);

        // 5. 将任务类型数据转为Map，方便后续查找
        Map<Integer, SysTaskType> typeMap = taskTypes.stream()
                .collect(Collectors.toMap(SysTaskType::getId, type -> type));

        // 6. 在Service层组装DTO
        List<TaskDTO> dtoList = taskPage.getRecords().stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setTask(task);
            dto.setTaskType(typeMap.get(task.getTaskTypeId()));
            return dto;
        }).collect(Collectors.toList());

        // 7. 构建并返回分页结果
        return PageResult.build(taskPage.getTotal(), dtoList);
    }

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(Long taskId, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(taskId.intValue());
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 验证是否为任务发布者
        if (!task.getPublisherId().equals(userId)) {
            throw new IllegalArgumentException("只有任务发布者才能取消任务");
        }

        // 3. 更新任务状态为已取消
        task.updateStatus(TaskStatusEnum.CANCELED);
        boolean success = sysTaskHandler.updateById(task);

        // 4. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.CANCEL, "取消任务:" + task.getTitle(), userId);
        sysTaskLogHandler.save(taskLog);

        return success;
    }

    /**
     * 获取抢单大厅任务列表
     * 查询所有待接单的任务，按发布时间倒序排序
     *
     * @return 任务列表
     */
    @Override
    public List<TaskDTO> getTaskHallList(Integer loginUserId) {
        // 1. 获取所有待接单的任务
        List<SysTask> tasks = sysTaskHandler.getTaskHallList(loginUserId);

        return getTaskDTOS(tasks);
    }

    /**
     * 抢单（接受任务）
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptTask(Long taskId, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(taskId.intValue());
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (TaskStatusEnum.PENDING.getCode() != task.getStatus()) {
            throw new IllegalArgumentException("该任务已被接单或已取消");
        }

        // 3. 校验是否是自己发布的任务
        if (userId.equals(task.getPublisherId())) {
            throw new IllegalArgumentException("不能接自己发布的任务");
        }

        // 4. 更新任务状态为已接单，并设置接单人
        task.acceptTask(userId);
        sysTaskHandler.updateById(task);

        // 5. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.ACCEPT, "接受任务:" + task.getTitle(), userId);
        sysTaskLogHandler.save(taskLog);

        return Boolean.TRUE;
    }

    /**
     * 获取我当前接单的任务列表
     * 只返回进行中的任务（已接单、进行中、待确认状态）
     *
     * @param userId 当前用户ID
     * @return 任务列表
     */
    @Override
    public List<TaskDTO> getMyAcceptedTasks(Integer userId) {
        // 1. 获取当前用户接单的未完成任务
        List<SysTask> tasks = sysTaskHandler.getMyAcceptedTasks(userId);

        return getTaskDTOS(tasks);
    }

    /**
     * 开始执行任务
     * 将任务状态从"已接单"变更为"进行中"
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(Integer taskId, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (TaskStatusEnum.ACCEPTED.getCode() != task.getStatus()) {
            throw new IllegalArgumentException("只有已接单的任务才能开始执行");
        }

        // 3. 校验是否是接单人
        if (!userId.equals(task.getRunnerId())) {
            throw new IllegalArgumentException("只有接单人才能开始执行任务");
        }

        // 4. 更新任务状态为进行中
        task.updateStatus(TaskStatusEnum.IN_PROGRESS);
        sysTaskHandler.updateById(task);

        // 5. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.START, "开始执行任务:" + task.getTitle(), userId);
        sysTaskLogHandler.save(taskLog);

        return Boolean.TRUE;
    }

    /**
     * 获取任务详情
     * 包含任务信息、任务类型信息、发布者信息、跑腿者信息、纠纷信息、留言列表和任务日志列表
     *
     * @param taskId 任务ID
     * @return 任务详情DTO
     */
    @Override
    public TaskDetailDTO getTaskDetail(Integer taskId) {
        // 1. 获取任务基本信息
        SysTask task = sysTaskHandler.getById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 获取任务类型信息
        SysTaskType taskType = sysTaskTypeHandler.getById(task.getTaskTypeId());

        // 3. 获取发布者和跑腿者信息
        SysStudent publisher = sysStudentHandler.getById(task.getPublisherId());
        SysStudent runner = task.getRunnerId() != null ? 
                sysStudentHandler.getById(task.getRunnerId()) : null;

        // 4. 获取纠纷信息
        SysDispute dispute = sysDisputeHandler.getByTaskId(taskId);

        // 5. 获取任务留言列表
        List<SysTaskMessage> messages = sysTaskMessageHandler.getTaskMessages(taskId);
        
        // 6. 获取所有留言发送者的ID
        List<Integer> studentIds = messages.stream()
                .map(SysTaskMessage::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // 7. 批量获取学生信息
        List<SysStudent> students = sysStudentHandler.getStudentsByIds(studentIds);
        Map<Integer, SysStudent> studentMap = students.stream()
                .collect(Collectors.toMap(SysStudent::getId, student -> student));

        // 8. 构建留言DTO列表
        List<TaskMessageDTO> messageDTOs = messages.stream()
                .map(message -> TaskMessageDTO.build(message, studentMap.get(message.getUserId())))
                .collect(Collectors.toList());

        // 9. 获取任务日志列表(按时间倒序)
        List<SysTaskLog> taskLogs = sysTaskLogHandler.getTaskLogs(taskId);

        // 10. 获取任务评价信息
        SysTaskEvaluation evaluation = sysTaskEvaluationHandler.getByTaskId(taskId);

        // 11. 构建并返回DTO
        return TaskDetailDTO.build(
                task,
                taskType,
                publisher,
                runner,
                dispute,
                messageDTOs,
                taskLogs,
                evaluation
        );
    }

    /**
     * 添加任务留言
     *
     * @param request 留言请求
     * @param userId  当前用户ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTaskMessage(TaskMessageAddRequest request, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(request.getTaskId());
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验用户是否有权限留言(只有任务发布者和接单者可以留言)
        if (!userId.equals(task.getPublisherId()) && !userId.equals(task.getRunnerId())) {
            throw new IllegalArgumentException("只有任务发布者和接单者可以留言");
        }

        // 3. 使用贫血模型初始化并保存留言
        SysTaskMessage message = new SysTaskMessage().init(request, userId);
        return sysTaskMessageHandler.save(message);
    }

    /**
     * 送达任务
     * 跑腿者标记任务已送达，等待发布者确认
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deliverTask(Integer taskId, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (TaskStatusEnum.IN_PROGRESS.getCode() != task.getStatus()) {
            throw new IllegalArgumentException("只有进行中的任务才能标记送达");
        }

        // 3. 校验是否是接单人
        if (!userId.equals(task.getRunnerId())) {
            throw new IllegalArgumentException("只有接单人才能标记任务送达");
        }

        // 4. 更新任务状态为已完成（等待确认）
        task.updateStatus(TaskStatusEnum.AWAITING_CONFIRMATION);
        sysTaskHandler.updateById(task);

        // 5. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.DELIVER, "送达任务:" + task.getTitle(), userId);
        sysTaskLogHandler.save(taskLog);

        return Boolean.TRUE;
    }

    /**
     * 确认任务完成
     * 发布者确认跑腿者已完成任务，任务状态变更为已完成
     *
     * @param taskId 任务ID
     * @param userId 当前用户ID（发布者）
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmTask(Integer taskId, Integer userId) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }

        // 2. 校验任务状态
        if (TaskStatusEnum.AWAITING_CONFIRMATION.getCode() != task.getStatus()) {
            throw new IllegalArgumentException("只有待确认的任务才能确认完成");
        }

        // 3. 校验是否是发布者
        if (!userId.equals(task.getPublisherId())) {
            throw new IllegalArgumentException("只有任务发布者才能确认完成");
        }

        // 4. 更新任务状态为已完成
        task.updateStatus(TaskStatusEnum.COMPLETED);
        sysTaskHandler.updateById(task);

        // 5. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.CONFIRM, "确认完成任务:" + task.getTitle(), userId);
        sysTaskLogHandler.save(taskLog);

        return Boolean.TRUE;
    }

    private List<TaskDTO> getTaskDTOS(List<SysTask> tasks) {
        if (tasks.isEmpty()) {
            return List.of();
        }

        // 2. 获取所有任务类型ID
        List<Integer> typeIds = tasks.stream()
                .map(SysTask::getTaskTypeId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 查询任务类型信息
        List<SysTaskType> taskTypes = sysTaskTypeHandler.getTaskTypesByIds(typeIds);
        Map<Integer, SysTaskType> typeMap = taskTypes.stream()
                .collect(Collectors.toMap(SysTaskType::getId, type -> type));

        // 4. 组装DTO
        return tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setTask(task);
            dto.setTaskType(typeMap.get(task.getTaskTypeId()));
            return dto;
        }).collect(Collectors.toList());
    }
} 