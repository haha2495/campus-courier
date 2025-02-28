package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import com.example.campus.courier.model.response.PageResult;

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
    Integer createDispute(DisputeCreateRequest request);

    /**
     * 分页查询纠纷任务
     *
     * @param request 分页查询参数
     * @return 分页结果
     */
    PageResult<TaskDTO> pageDisputeTasks(DisputePageRequest request);

}