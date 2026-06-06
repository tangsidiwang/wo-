package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.request.CommentCreateReq;
import com.wo.dto.response.CommentListResp;
import com.wo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public Result<CommentListResp> list(@PathVariable Long postId,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(commentService.list(postId, page, size, userId));
    }

    @PostMapping("/posts/{postId}/comments")
    public Result<CommentListResp.Item> create(@PathVariable Long postId,
                                               @Valid @RequestBody CommentCreateReq req) {
        StpUtil.checkLogin();
        return Result.ok(commentService.create(postId, StpUtil.getLoginIdAsLong(), req));
    }

    @DeleteMapping("/comments/{id}")
    public Result<?> delete(@PathVariable Long id) {
        StpUtil.checkLogin();
        commentService.delete(id, StpUtil.getLoginIdAsLong());
        return Result.ok("删除成功");
    }

    @PostMapping("/comments/{id}/like")
    public Result<?> like(@PathVariable Long id) {
        StpUtil.checkLogin();
        commentService.toggleLike(id, StpUtil.getLoginIdAsLong());
        return Result.ok("ok");
    }

    @DeleteMapping("/comments/{id}/like")
    public Result<?> unlike(@PathVariable Long id) {
        StpUtil.checkLogin();
        commentService.toggleLike(id, StpUtil.getLoginIdAsLong());
        return Result.ok("ok");
    }
}
