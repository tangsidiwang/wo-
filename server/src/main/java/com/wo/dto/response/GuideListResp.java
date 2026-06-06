package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class GuideListResp {
    private List<Item> items;
    private Long total;
    private Integer page;
    private Boolean hasMore;

    @Data @Builder
    public static class Item {
        private Long id;
        private String title;
        private String coverUrl;
        private String summary;
        private String authorNickname;
        private String gameName;
        private Integer viewCount;
        private Integer likeCount;
        private Integer sectionCount;
        private Boolean isFavorited;
        private String createdAt;
    }
}
