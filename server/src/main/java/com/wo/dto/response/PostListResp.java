package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PostListResp {
    private List<Item> items;
    private Long total;
    private Integer page;
    private Boolean hasMore;

    @Data
    @Builder
    public static class Item {
        private Long id;
        private Long userId;
        private String authorNickname;
        private String authorAvatar;
        private String categoryName;
        private String title;
        private String contentText;
        private String coverUrl;
        private List<String> imageThumbUrls;
        private Integer viewCount;
        private Integer likeCount;
        private Integer commentCount;
        private Boolean isLiked;
        private Boolean isFavorited;
        private String createdAt;
    }
}
