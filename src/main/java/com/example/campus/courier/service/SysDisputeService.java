package com.example.campus.courier.service;

import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import jakarta.validation.Valid;

/**
 * 纠纷服务接口
 */
public interface SysDisputeService {

    Integer createDispute(@Valid DisputeCreateRequest request);

}