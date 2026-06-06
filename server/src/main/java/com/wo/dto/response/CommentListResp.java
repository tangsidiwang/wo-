package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CommentListResp {
    private List<Item> items;
    private Long total;
    private Integer page;
    private Boolean hasMore;

    @Data
    @Builder
    public static class Item {
        private Long id;
        private Long userId;
        private String nickname;
        private String avatar;
        private String content;
        private Integer likeCount;
        private Boolean isLiked;
        private String createdAt;
        private List<Item> replies;
    }
}
