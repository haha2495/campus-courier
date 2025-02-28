package com.example.campus.courier.controller;

import com.example.campus.courier.model.request.dispute.DisputeCreateRequest;
import com.example.campus.courier.model.response.Result;
import com.example.campus.courier.service.SysDisputeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 纠纷控制器
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispute")
@Tag(name = "纠纷管理接口")
public class SysDisputeController {

    private final SysDisputeService disputeService;

    @PostMapping("/create")
    @Operation(summary = "创建纠纷", description = "用户提交纠纷信息")
    public Result<Integer> createDispute(
            @Parameter(description = "纠纷创建请求", required = true)
            @Valid @RequestBody DisputeCreateRequest request
    ) {
        log.info("创建纠纷请求: {}", request);
        return Result.success(disputeService.createDispute(request), "纠纷提交成功");
    }
} 