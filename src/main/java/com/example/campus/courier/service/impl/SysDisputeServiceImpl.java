package com.example.campus.courier.service.impl;

import com.example.campus.courier.mapper.handler.SysDisputeHandler;
import com.example.campus.courier.mapper.handler.SysTaskHandler;
import com.example.campus.courier.mapper.handler.SysTaskLogHandler;
import com.example.campus.courier.model.entity.SysDispute;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.entity.SysTaskLog;
import com.example.campus.courier.model.enums.TaskActionEnum;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.service.SysDisputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 纠纷服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDisputeServiceImpl implements SysDisputeService {
    
    private final SysDisputeHandler disputeHandler;
    private final SysTaskHandler taskHandler;
    private final SysTaskLogHandler taskLogHandler;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createDispute(DisputeCreateRequest request) {
        // 1. 获取任务信息
        SysTask task = taskHandler.getById(request.getTaskId());
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 2. 构建并保存纠纷实体
        SysDispute dispute = SysDispute.init(request, task.getPublisherId(), task.getRunnerId());
        disputeHandler.save(dispute);
        
        // 3. 更新任务状态为纠纷中
        task.updateStatus(TaskStatusEnum.DISPUTED);
        taskHandler.updateById(task);

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
        taskLogHandler.save(taskLog);
        
        log.info("纠纷创建成功: {}", dispute.getId());
        return dispute.getId();
    }
}