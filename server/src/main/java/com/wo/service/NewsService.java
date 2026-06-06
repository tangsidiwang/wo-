package com.wo.service;

import com.wo.dto.response.NewsDetailResp;
import com.wo.dto.response.NewsListResp;

public interface NewsService {
    NewsListResp list(int page, int size, String category);
    NewsDetailResp detail(Long id);
}
