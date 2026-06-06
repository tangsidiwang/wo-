package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.response.ConversationListResp;
import com.wo.dto.response.FollowListResp;
import com.wo.dto.response.MessageListResp;
import com.wo.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @PostMapping("/follow/{id}")
    public Result<?> follow(@PathVariable Long id) {
        socialService.follow(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("关注成功");
    }

    @DeleteMapping("/follow/{id}")
    public Result<?> unfollow(@PathVariable Long id) {
        socialService.unfollow(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("已取消关注");
    }

    @GetMapping("/users/{id}/followers")
    public Result<FollowListResp> followers(@PathVariable Long id) {
        Long uid = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(socialService.followers(id, uid));
    }

    @GetMapping("/users/{id}/following")
    public Result<FollowListResp> following(@PathVariable Long id) {
        Long uid = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(socialService.following(id, uid));
    }

    @PostMapping("/messages")
    public Result<?> sendMessage(@RequestBody Map<String, Object> body) {
        Long toUid = Long.valueOf(body.get("toUid").toString());
        String content = (String) body.get("content");
        socialService.sendMessage(StpUtil.getLoginIdAsLong(), toUid, content);
        return Result.ok("发送成功");
    }

    @GetMapping("/messages/conversations")
    public Result<ConversationListResp> conversations() {
        return Result.ok(socialService.conversations(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/messages/chat/{peerId}")
    public Result<MessageListResp> chatHistory(@PathVariable Long peerId) {
        return Result.ok(socialService.chatHistory(StpUtil.getLoginIdAsLong(), peerId));
    }
}
