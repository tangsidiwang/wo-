package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class GuideDetailResp {
    private Long id;
    private String title;
    private String coverUrl;
    private String summary;
    private UserBrief author;
    private String gameName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Boolean isFavorited;
    private List<Section> sections;
    private String createdAt;

    @Data @Builder
    public static class UserBrief {
        private Long id;
        private String nickname;
        private String avatar;
    }

    @Data @Builder
    public static class Section {
        private Long id;
        private Integer sortOrder;
        private String subtitle;
        private String content;
        private String imageUrl;
    }
}
