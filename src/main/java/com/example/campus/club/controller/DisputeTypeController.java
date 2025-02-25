package com.example.campus.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.DisputeType;
import com.example.campus.club.model.request.DisputeTypeRequest;
import com.example.campus.club.service.DisputeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "纠纷类型管理")
@RestController
@RequestMapping("/api/dispute-types")
@RequiredArgsConstructor
public class DisputeTypeController extends BaseController {

    private final DisputeTypeService disputeTypeService;

    @Operation(summary = "新增纠纷类型")
    @PostMapping
    public Result<DisputeType> add(@Validated @RequestBody DisputeTypeRequest request) {
        return Result.success(disputeTypeService.add(request));
    }

    @Operation(summary = "修改纠纷类型")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Validated @RequestBody DisputeTypeRequest request) {
        disputeTypeService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "搜索纠纷类型")
    @GetMapping
    public Result<Page<DisputeType>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(disputeTypeService.search(keyword, pageNum, pageSize));
    }
} 