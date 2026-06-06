package com.wo.service.impl;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FileUploadService {

    private static final String UPLOAD_ROOT = System.getProperty("user.dir") + "/uploads";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy/MM");

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("文件为空");
        String originalFilename = file.getOriginalFilename();
        String ext = "jpg";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }
        if (!List.of("jpg", "jpeg", "png", "gif", "webp").contains(ext))
            throw new RuntimeException("不支持的图片格式: " + ext);
        if (file.getSize() > 10 * 1024 * 1024)
            throw new RuntimeException("图片大小不能超过10MB");

        String dateStr = LocalDate.now().format(FMT);
        String filename = IdUtil.fastSimpleUUID() + "." + ext;
        String relativePath = dateStr + "/" + filename;

        Path dest = Paths.get(UPLOAD_ROOT, relativePath);
        try {
            Files.createDirectories(dest.getParent());
            file.transferTo(dest.toFile());
            log.info("Image uploaded: {} (size={})", relativePath, file.getSize());
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new RuntimeException("文件保存失败: " + e.getMessage(), e);
        }
        return "/uploads/" + relativePath;
    }
}
