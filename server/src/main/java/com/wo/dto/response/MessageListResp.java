package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class MessageListResp {
    private List<Item> items;

    @Data @Builder
    public static class Item {
        private Long id;
        private Long fromUid;
        private Long toUid;
        private String content;
        private Integer isRead;
        private String createdAt;
    }
}
