package com.example.campus.club.controller;

import com.example.campus.club.common.Result;
import com.example.campus.club.model.entity.Courier;
import com.example.campus.club.model.request.CourierProfileRequest;
import com.example.campus.club.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "跑腿者接口")
@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController extends BaseController {

    private final CourierService courierService;

    @Operation(summary = "获取跑腿者信息")
    @GetMapping("/info")
    public Result<Courier> getCourierInfo() {
        return Result.success(courierService.getCourierInfo(getCurrentUserId()));
    }

    @Operation(summary = "更新跑腿者信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Validated @RequestBody CourierProfileRequest request) {
        courierService.updateProfile(getCurrentUserId(), request);
        return Result.success();
    }
} 