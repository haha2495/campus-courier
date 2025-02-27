package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskMessageMapper;
import com.example.campus.courier.model.entity.SysTaskMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务留言数据处理层
 */
@Component
@RequiredArgsConstructor
public class SysTaskMessageHandler extends ServiceImpl<SysTaskMessageMapper, SysTaskMessage> {

    /**
     * 获取任务的留言列表
     * 按创建时间升序排序
     *
     * @param taskId 任务ID
     * @return 留言列表
     */
    public List<SysTaskMessage> getTaskMessages(Integer taskId) {
        return lambdaQuery()
                .eq(SysTaskMessage::getTaskId, taskId)
                .orderByAsc(SysTaskMessage::getCreateTime)
                .list();
    }
} 