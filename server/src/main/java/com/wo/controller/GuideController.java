package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.request.GuideCreateReq;
import com.wo.dto.response.GuideDetailResp;
import com.wo.dto.response.GuideListResp;
import com.wo.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guides")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @GetMapping
    public Result<GuideListResp> list(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(required = false) Integer gameId) {
        return Result.ok(guideService.list(page, size, gameId, null));
    }

    @GetMapping("/{id}")
    public Result<GuideDetailResp> detail(@PathVariable Long id) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        return Result.ok(guideService.detail(id, userId));
    }

    @PostMapping
    public Result<GuideDetailResp> create(@Valid @RequestBody GuideCreateReq req) {
        StpUtil.checkLogin();
        return Result.ok(guideService.create(StpUtil.getLoginIdAsLong(), req));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        StpUtil.checkLogin();
        guideService.delete(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("删除成功");
    }

    @PostMapping("/{id}/favorite")
    public Result<?> favorite(@PathVariable Long id) {
        StpUtil.checkLogin();
        guideService.toggleFavorite(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }

    @DeleteMapping("/{id}/favorite")
    public Result<?> unfavorite(@PathVariable Long id) {
        StpUtil.checkLogin();
        guideService.toggleFavorite(StpUtil.getLoginIdAsLong(), id);
        return Result.ok("ok");
    }
}
