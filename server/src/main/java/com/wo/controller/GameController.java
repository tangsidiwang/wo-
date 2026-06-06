package com.wo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wo.common.Result;
import com.wo.dto.response.GameCategoryResp;
import com.wo.entity.Tag;
import com.wo.mapper.TagMapper;
import com.wo.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final TagMapper tagMapper;

    @GetMapping("/games")
    public Result<List<GameCategoryResp>> getCategories() {
        return Result.ok(gameService.listCategories());
    }

    @GetMapping("/tags")
    public Result<List<String>> getTags() {
        List<Tag> tags = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().eq(Tag::getStatus, 1).orderByDesc(Tag::getPostCount));
        return Result.ok(tags.stream().map(Tag::getName).collect(Collectors.toList()));
    }
}
