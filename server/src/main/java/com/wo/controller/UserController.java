package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.response.PostListResp;
import com.wo.dto.response.UserInfoResp;
import com.wo.service.PostService;
import com.wo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/me")
    public Result<UserInfoResp> myInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(userService.getMyInfo(userId));
    }

    @PutMapping("/me")
    public Result<UserInfoResp> updateProfile(@RequestBody Map<String, Object> data) {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(userService.updateProfile(userId, data));
    }

    @GetMapping("/{id}")
    public Result<UserInfoResp> userInfo(@PathVariable Long id) {
        return Result.ok(userService.getUserInfo(id));
    }

    @GetMapping("/me/posts")
    public Result<PostListResp> myPosts(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(postService.listByUser(page, size, userId));
    }

    @GetMapping("/me/favorites")
    public Result<PostListResp> myFavorites(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(postService.listFavorites(page, size, userId));
    }

    @GetMapping("/{id}/posts")
    public Result<PostListResp> userPosts(@PathVariable Long id,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return Result.ok(postService.listByUser(page, size, id));
    }
}
