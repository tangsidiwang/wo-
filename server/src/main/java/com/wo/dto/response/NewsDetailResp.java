package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class NewsDetailResp {
    private Long id;
    private String title;
    private String coverUrl;
    private String summary;
    private String content;
    private String category;
    private String source;
    private String sourceUrl;
    private Integer viewCount;
    private String publishedAt;
}
