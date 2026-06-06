package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wo.common.Result;
import com.wo.entity.Notification;
import com.wo.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationMapper notificationMapper;

    @GetMapping("/notifications")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int size) {
        long userId = StpUtil.getLoginIdAsLong();
        var result = notificationMapper.selectPage(Page.of(page, size),
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .orderByDesc(Notification::getCreatedAt));
        return Result.ok(result.getRecords());
    }

    @GetMapping("/notifications/unread-count")
    public Result<?> unreadCount() {
        long userId = StpUtil.getLoginIdAsLong();
        long count = notificationMapper.selectCount(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getIsRead, 0));
        Map<String, Long> data = new HashMap<>();
        data.put("count", count);
        return Result.ok(data);
    }

    @PutMapping("/notifications/{id}/read")
    public Result<?> markRead(@PathVariable Long id) {
        Notification n = notificationMapper.selectById(id);
        if (n != null) { n.setIsRead(1); notificationMapper.updateById(n); }
        return Result.ok("ok");
    }

    @PutMapping("/notifications/read-all")
    public Result<?> markAllRead() {
        long userId = StpUtil.getLoginIdAsLong();
        var list = notificationMapper.selectList(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId).eq(Notification::getIsRead, 0));
        list.forEach(n -> { n.setIsRead(1); notificationMapper.updateById(n); });
        return Result.ok("ok");
    }
}
