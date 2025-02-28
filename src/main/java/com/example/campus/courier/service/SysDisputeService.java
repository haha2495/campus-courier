package com.example.campus.courier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.courier.model.entity.SysTask;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import com.example.campus.courier.model.response.PageResult;
import jakarta.validation.Valid;

/**
 * 纠纷服务接口
 */
public interface SysDisputeService {

    /**
     * 创建纠纷
     *
     * @param request 创建请求
     * @return 纠纷ID
     */
    Integer createDispute(@Valid DisputeCreateRequest request);

    /**
     * 分页查询纠纷任务
     *
     * @param request 分页查询参数
     * @return 分页结果
     */
    PageResult<SysTask> pageDisputeTasks(DisputePageRequest request);

}