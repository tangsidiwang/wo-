package com.wo.controller;

import com.wo.common.Result;
import com.wo.dto.response.NewsDetailResp;
import com.wo.dto.response.NewsListResp;
import com.wo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public Result<NewsListResp> list(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(required = false) String category) {
        return Result.ok(newsService.list(page, size, category));
    }

    @GetMapping("/{id}")
    public Result<NewsDetailResp> detail(@PathVariable Long id) {
        return Result.ok(newsService.detail(id));
    }
}
