package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class ConversationListResp {
    private List<Item> items;

    @Data @Builder
    public static class Item {
        private Long userId;
        private String nickname;
        private String avatar;
        private String lastContent;
        private Integer unreadCount;
        private String lastTime;
    }
}
