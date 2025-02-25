package com.example.campus.club.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.example.campus.club.model.response.Result;
import com.example.campus.club.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.failed(e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(Exception e) {
        BindingResult bindingResult;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else {
            bindingResult = ((BindException) e).getBindingResult();
        }

        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(", ");
        }
        message.delete(message.length() - 2, message.length());

        return Result.validateFailed(message.toString());
    }

    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleNotLoginException(NotLoginException e) {
        log.warn("未登录：{}", e.getMessage());
        return Result.unauthorized();
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    public Result<Void> handleNotPermissionException(Exception e) {
        log.warn("权限不足：{}", e.getMessage());
        return Result.forbidden();
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.failed(ResultCode.SYSTEM_INNER_ERROR);
    }
} 