package com.example.campus.courier.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.mapper.handler.SysTaskLogHandler;
import com.example.campus.courier.mapper.handler.SysTaskTypeHandler;
import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskLog;
import com.example.campus.courier.model.entity.SysTaskType;
import com.example.campus.courier.model.enums.TaskActionEnum;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.example.campus.courier.model.request.task.TaskQueryRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.service.SysTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 1. 使用贫血模型初始化任务实体
        SysTask task = new SysTask().init(request, publisherId);

        // 2. 保存任务
        sysTaskHandler.save(task);

        // 3. 初始化并保存任务日志
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.PUBLISH, "发布任务:" + task.getTitle());
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
        SysTaskLog taskLog = new SysTaskLog().init(task.getId(), TaskActionEnum.CANCEL, "取消任务:" + task.getTitle());
        sysTaskLogHandler.save(taskLog);

        return success;
    }
} 