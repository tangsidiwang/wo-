package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class FollowListResp {
    private List<Item> items;
    private Long total;

    @Data @Builder
    public static class Item {
        private Long id;
        private String nickname;
        private String avatar;
        private String bio;
        private Integer postCount;
        private Integer fansCount;
        private Boolean isFollowed;
        private String createdAt;
    }
}
