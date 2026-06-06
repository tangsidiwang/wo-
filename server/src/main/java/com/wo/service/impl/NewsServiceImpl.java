package com.wo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wo.common.exception.BusinessException;
import com.wo.dto.response.NewsDetailResp;
import com.wo.dto.response.NewsListResp;
import com.wo.entity.News;
import com.wo.mapper.NewsMapper;
import com.wo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public NewsListResp list(int page, int size, String category) {
        var wrapper = new LambdaQueryWrapper<News>()
                .eq(News::getStatus, 1)
                .eq(category != null && !category.isEmpty(), News::getCategory, category)
                .orderByDesc(News::getIsTop)
                .orderByDesc(News::getPublishedAt);

        var pageResult = newsMapper.selectPage(Page.of(page, size), wrapper);
        List<News> list = pageResult.getRecords();

        List<NewsListResp.Item> items = list.stream().map(n -> NewsListResp.Item.builder()
                .id(n.getId()).title(n.getTitle()).coverUrl(n.getCoverUrl())
                .summary(n.getSummary()).category(n.getCategory()).source(n.getSource())
                .publishedAt(n.getPublishedAt() != null ? n.getPublishedAt().format(FMT) : "").build()
        ).collect(Collectors.toList());

        return NewsListResp.builder().items(items).total(pageResult.getTotal()).page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
    }

    @Override
    public NewsDetailResp detail(Long id) {
        News news = newsMapper.selectById(id);
        if (news == null || news.getStatus() != 1) throw new BusinessException("资讯不存在");
        newsMapper.incrementViewCount(id);
        news.setViewCount(news.getViewCount() + 1);

        return NewsDetailResp.builder()
                .id(news.getId()).title(news.getTitle()).coverUrl(news.getCoverUrl())
                .summary(news.getSummary()).content(news.getContent())
                .category(news.getCategory()).source(news.getSource()).sourceUrl(news.getSourceUrl())
                .viewCount(news.getViewCount())
                .publishedAt(news.getPublishedAt() != null ? news.getPublishedAt().format(FMT) : "").build();
    }
}
