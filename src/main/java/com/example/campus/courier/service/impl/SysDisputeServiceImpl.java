package com.example.campus.courier.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.exception.BusinessException;
import com.example.campus.courier.mapper.handler.*;
import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.entity.*;
import com.example.campus.courier.model.enums.*;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.request.dispute.DisputeHandleRequest;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import com.example.campus.courier.model.response.PageResult;
import com.example.campus.courier.service.SysDisputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 纠纷服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDisputeServiceImpl implements SysDisputeService {

    private final SysDisputeHandler sysDisputeHandler;
    private final SysTaskHandler sysTaskHandler;
    private final SysTaskLogHandler sysTaskLogHandler;
    private final SysStudentHandler sysStudentHandler;
    private final SysBalanceRecordHandler sysBalanceRecordHandler;
    private final SysTaskTypeHandler sysTaskTypeHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createDispute(DisputeCreateRequest request) {
        // 1. 获取任务信息
        SysTask task = sysTaskHandler.getById(request.getTaskId());
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        // 2. 构建并保存纠纷实体
        SysDispute dispute = SysDispute.init(request, task.getPublisherId(), task.getRunnerId());
        sysDisputeHandler.save(dispute);

        // 3. 更新任务状态为纠纷中
        task.updateStatus(TaskStatusEnum.DISPUTED);
        sysTaskHandler.updateById(task);

        // 4. 记录任务日志
        SysTaskLog taskLog = new SysTaskLog();
        taskLog.init(
                task.getId(),
                TaskActionEnum.SUBMIT_DISPUTE,
                String.format("提交纠纷：%s", dispute.getTitle()),
                dispute.getInitiatorId()
        );
        // 如果有证据图片，添加到日志中
        if (request.getEvidence() != null && !request.getEvidence().isEmpty()) {
            taskLog.setImages(request.getEvidence());
        }
        sysTaskLogHandler.save(taskLog);

        log.info("纠纷创建成功: {}", dispute.getId());
        return dispute.getId();
    }

    @Override
    public PageResult<TaskDTO> pageDisputeTasks(DisputePageRequest request) {
        // 1. 查询纠纷分页数据
        Page<SysDispute> disputePage = sysDisputeHandler.pageDisputes(request);

        if (CollectionUtils.isEmpty(disputePage.getRecords())) {
            return PageResult.empty();
        }

        // 2. 获取关联的任务ID列表
        List<Integer> taskIds = disputePage.getRecords().stream()
                .map(SysDispute::getTaskId)
                .distinct()
                .toList();

        // 3. 批量查询任务信息
        List<SysTask> tasks = sysTaskHandler.listByIds(taskIds);

        // 4. 获取任务类型ID列表
        List<Integer> typeIds = tasks.stream()
                .map(SysTask::getTaskTypeId)
                .distinct()
                .toList();

        // 5. 批量查询任务类型信息
        List<SysTaskType> taskTypes = sysTaskTypeHandler.selectBatchIds(typeIds);
        Map<Integer, SysTaskType> typeMap = taskTypes.stream()
                .collect(Collectors.toMap(SysTaskType::getId, type -> type));

        // 6. 构建TaskDTO列表
        List<TaskDTO> dtoList = tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setTask(task);
            dto.setTaskType(typeMap.get(task.getTaskTypeId()));
            return dto;
        }).toList();

        // 7. 构建分页结果
        return PageResult.build(disputePage.getTotal(), dtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleDispute(DisputeHandleRequest request) {
        // 1. 获取纠纷信息
        SysDispute dispute = sysDisputeHandler.getById(request.getDisputeId());
        if (dispute == null) {
            throw new BusinessException("纠纷不存在");
        }

        // 2. 获取任务信息
        SysTask task = sysTaskHandler.getById(dispute.getTaskId());
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 3. 获取发布者和跑腿者信息
        SysStudent publisher = sysStudentHandler.getById(task.getPublisherId());
        SysStudent runner = sysStudentHandler.getById(task.getRunnerId());
        if (publisher == null || runner == null) {
            throw new BusinessException("用户信息不存在");
        }

        // 4. 处理纠纷结果
        DisputeHandleResultEnum handleResult = DisputeHandleResultEnum.getByCode(request.getHandleResult());
        if (handleResult == null) {
            throw new BusinessException("处理结果无效");
        }

        // 计算任务总金额（包含加急费）
        BigDecimal totalAmount = task.calculateTotalPrice();

        switch (handleResult) {
            case PUBLISHER_WIN -> {
                // 发布者胜诉，将金额退还给发布者
                publisher.increaseBalance(totalAmount);
                sysStudentHandler.updateById(publisher);
                // 记录余额变动
                sysBalanceRecordHandler.createRecord(
                        publisher,
                        totalAmount,
                        BalanceChangeTypeEnum.INCOME.getCode(),
                        "纠纷处理-发布者胜诉退款，任务：" + task.getTitle()
                );
                // 更新任务状态为已取消
                task.setStatus(TaskStatusEnum.CANCELED.getCode());
                sysTaskHandler.updateById(task);
                // 更新纠纷状态为已结束
                dispute.setStatus(DisputeStatusEnum.FINISHED.getCode());
            }
            case RUNNER_WIN -> {
                // 跑腿者胜诉，将金额给予跑腿者
                runner.increaseBalance(totalAmount);
                sysStudentHandler.updateById(runner);
                // 记录余额变动
                sysBalanceRecordHandler.createRecord(
                        runner,
                        totalAmount,
                        BalanceChangeTypeEnum.INCOME.getCode(),
                        "纠纷处理-跑腿者胜诉收款，任务：" + task.getTitle()
                );
                // 更新任务状态为已完成
                task.setStatus(TaskStatusEnum.COMPLETED.getCode());
                sysTaskHandler.updateById(task);
                // 更新纠纷状态为已结束
                dispute.setStatus(DisputeStatusEnum.FINISHED.getCode());
            }
            case UNCERTAIN -> {
                // 存在疑问，设置截止时间
                if (request.getDisplayHours() == null) {
                    throw new BusinessException("请设置展示时长");
                }
                if (request.getDisplayHours() < 1 || request.getDisplayHours() > 168) {
                    throw new BusinessException("展示时长必须在1-168小时之间");
                }
                // 计算截止时间：当前时间 + 展示时长（小时）
                LocalDateTime deadline = LocalDateTime.now().plusHours(request.getDisplayHours());
                dispute.setDeadline(deadline);
                // 更新纠纷状态为已发布纠纷大厅
                dispute.setStatus(DisputeStatusEnum.PUBLISHED.getCode());
            }
        }

        // 5. 更新纠纷信息
        dispute.setResult(handleResult.getCode());
        dispute.setAdminRemark(request.getHandleRemark());
        sysDisputeHandler.updateById(dispute);

        // 6. 记录任务日志
        String logContent = "纠纷处理结果：" + handleResult.getDesc();
        if (request.getHandleRemark() != null) {
            logContent += "，处理说明：" + request.getHandleRemark();
        }
        if (handleResult == DisputeHandleResultEnum.UNCERTAIN) {
            logContent += String.format("，展示时长：%d小时", request.getDisplayHours());
        }
        SysTaskLog taskLog = new SysTaskLog().init(
                task.getId(),
                TaskActionEnum.RESOLVE_DISPUTE,
                logContent,
                0 // 系统处理，用户ID为0
        );
        sysTaskLogHandler.save(taskLog);
    }
}