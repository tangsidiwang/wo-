package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.request.PostCreateReq;
import com.wo.dto.response.PostDetailResp;
import com.wo.dto.response.PostListResp;
import com.wo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Result<PostListResp> list(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(defaultValue = "latest") String sort,
                                     @RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false) String keyword) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(postService.list(page, size, sort, categoryId, keyword, userId));
    }

    @GetMapping("/{id}")
    public Result<PostDetailResp> detail(@PathVariable Long id) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(postService.detail(id, userId));
    }

    @PostMapping
    public Result<PostDetailResp> create(@RequestBody PostCreateReq req) {
        StpUtil.checkLogin();
        return Result.ok(postService.create(StpUtil.getLoginIdAsLong(), req));
    }

    @PutMapping("/{id}")
    public Result<PostDetailResp> update(@PathVariable Long id, @RequestBody PostCreateReq req) {
        StpUtil.checkLogin();
        return Result.ok(postService.update(StpUtil.getLoginIdAsLong(), id, req));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        StpUtil.checkLogin();
        postService.delete(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("删除成功");
    }

    @PostMapping("/{id}/like")
    public Result<?> like(@PathVariable Long id) {
        StpUtil.checkLogin();
        postService.toggleLike(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }

    @DeleteMapping("/{id}/like")
    public Result<?> unlike(@PathVariable Long id) {
        StpUtil.checkLogin();
        postService.toggleLike(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }

    @PostMapping("/{id}/favorite")
    public Result<?> favorite(@PathVariable Long id) {
        StpUtil.checkLogin();
        postService.toggleFavorite(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }

    @DeleteMapping("/{id}/favorite")
    public Result<?> unfavorite(@PathVariable Long id) {
        StpUtil.checkLogin();
        postService.toggleFavorite(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }
}
