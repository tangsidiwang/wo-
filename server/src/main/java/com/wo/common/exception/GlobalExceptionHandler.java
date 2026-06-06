package com.wo.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.wo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public Result<?> handleNotLogin(NotLoginException e) {
        return Result.fail(401, "请先登录");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("参数校验失败");
        return Result.fail(400, msg);
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBind(BindException e) {
        return Result.fail(400, "参数校验失败");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleBadBody(HttpMessageNotReadableException e) {
        return Result.fail(400, "请求格式错误");
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusiness(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public Result<?> handleSql(SQLException e) {
        log.error("SQL error", e);
        return Result.fail(500, "数据库异常: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleAll(Exception e) {
        log.error("unhandled exception: {}", e.getClass().getName());
        log.error("message: {}", e.getMessage());
        // 打印第一行堆栈帮助定位
        StackTraceElement[] trace = e.getStackTrace();
        if (trace.length > 0) log.error("at {}", trace[0]);
        if (trace.length > 1) log.error("at {}", trace[1]);
        if (trace.length > 2) log.error("at {}", trace[2]);
        return Result.fail(500, "服务器内部错误: " + e.getClass().getSimpleName());
    }
}
