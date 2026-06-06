package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PostDetailResp {
    private Long id;
    private Long userId;
    private UserBrief author;
    private String categoryName;
    private String title;
    private String content;
    private String contentText;
    private String coverUrl;
    private List<ImageInfo> images;
    private List<String> tags;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Boolean isLiked;
    private Boolean isFavorited;
    private String createdAt;

    @Data
    @Builder
    public static class UserBrief {
        private Long id;
        private String nickname;
        private String avatar;
    }

    @Data
    @Builder
    public static class ImageInfo {
        private Long id;
        private String url;
        private String thumbUrl;
        private Integer width;
        private Integer height;
    }
}
