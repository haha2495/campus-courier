package com.example.campus.courier.job;

import com.example.campus.courier.mapper.handler.SysDisputeHandler;
import com.example.campus.courier.model.entity.SysDispute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 纠纷大厅状态更新定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DisputeHallJob {

    private final SysDisputeHandler disputeHandler;

    /**
     * 每1分钟检查一次纠纷大厅状态
     * 将已到期的纠纷状态更新为待处理(1)
     */
    @Scheduled(fixedRate = 60000)
    public void updateDisputeHallStatus() {
        log.info("开始执行纠纷大厅状态更新任务...");
        try {
            // 获取当前在大厅展示的纠纷
            List<SysDispute> disputes = disputeHandler.listHallDisputes();
            LocalDateTime now = LocalDateTime.now();

            // 遍历检查是否有到期的纠纷
            for (SysDispute dispute : disputes) {
                if (dispute.getDeadline() != null && dispute.getDeadline().isBefore(now)) {
                    // 更新状态为待处理
                    if (disputeHandler.updateExpiredDisputeStatus(dispute.getId())) {
                        log.info("纠纷[{}]展示时间已到，状态已更新为待处理", dispute.getId());
                    }
                }
            }
            log.info("纠纷大厅状态更新任务执行完成");
        } catch (Exception e) {
            log.error("纠纷大厅状态更新任务执行异常", e);
        }
    }
} 