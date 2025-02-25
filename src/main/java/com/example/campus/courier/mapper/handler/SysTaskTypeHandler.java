package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysTaskTypeMapper;
import com.example.campus.courier.model.entity.SysTaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 任务类型数据处理层
 */
@Component
@RequiredArgsConstructor
public class SysTaskTypeHandler extends ServiceImpl<SysTaskTypeMapper, SysTaskType> {

}