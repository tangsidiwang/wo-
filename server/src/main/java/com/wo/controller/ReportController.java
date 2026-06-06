package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.request.ReportReq;
import com.wo.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public Result<?> submit(@Valid @RequestBody ReportReq req) {
        StpUtil.checkLogin();
        reportService.submit(StpUtil.getLoginIdAsLong(), req);
        return Result.ok("举报已提交");
    }
}
