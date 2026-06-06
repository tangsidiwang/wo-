package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.service.impl.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/image")
    public Result<?> uploadImage(@RequestParam MultipartFile file) {
        StpUtil.checkLogin();
        String url = fileUploadService.uploadImage(file);
        return Result.ok(Map.of("url", url));
    }
}
