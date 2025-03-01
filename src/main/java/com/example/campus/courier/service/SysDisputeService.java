package com.example.campus.courier.service;

import com.example.campus.courier.model.dto.TaskDTO;
import com.example.campus.courier.model.entity.SysDispute;
import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.request.dispute.DisputeHandleRequest;
import com.example.campus.courier.model.request.dispute.DisputePageRequest;
import com.example.campus.courier.model.request.dispute.DisputeSupportRequest;
import com.example.campus.courier.model.response.PageResult;
import jakarta.validation.Valid;

import java.util.List;

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

    /**
     * 处理纠纷
     *
     * @param request 处理请求
     */
    void handleDispute(DisputeHandleRequest request);

    /**
     * 查询纠纷大厅数据
     * 获取当前在纠纷大厅展示的任务列表
     *
     * @return 纠纷任务列表
     */
    List<TaskDTO> listHallDisputes();

    void createSupport(@Valid DisputeSupportRequest request, Integer loginUserId);
}