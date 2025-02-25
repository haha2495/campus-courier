package com.example.campus.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.Dispute;
import com.example.campus.club.model.vo.DisputeDetailVO;
import com.example.campus.club.model.request.DisputeApprovalRequest;
import com.example.campus.club.service.DisputeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "纠纷管理")
@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
public class DisputeController extends BaseController {

    private final DisputeService disputeService;

    @Operation(summary = "搜索纠纷")
    @GetMapping
    public Result<Page<Dispute>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long typeId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(disputeService.search(keyword, status, typeId, pageNum, pageSize));
    }

    @Operation(summary = "获取纠纷详情")
    @GetMapping("/{id}")
    public Result<DisputeDetailVO> getDetail(@PathVariable Long id) {
        return Result.success(disputeService.getDetail(id));
    }

    @Operation(summary = "审批纠纷")
    @PostMapping("/approve")
    public Result<Void> approve(@Validated @RequestBody DisputeApprovalRequest request) {
        disputeService.approve(request);
        return Result.success();
    }

    @Operation(summary = "转发纠纷")
    @PostMapping("/{id}/forward")
    public Result<Void> forward(@PathVariable Long id) {
        disputeService.forward(id);
        return Result.success();
    }
} 