package com.wo.service;

import com.wo.dto.response.ConversationListResp;
import com.wo.dto.response.FollowListResp;
import com.wo.dto.response.MessageListResp;

public interface SocialService {
    void follow(Long userId, Long targetId);
    void unfollow(Long userId, Long targetId);
    FollowListResp followers(Long userId, Long currentUserId);
    FollowListResp following(Long userId, Long currentUserId);

    void sendMessage(Long fromUid, Long toUid, String content);
    MessageListResp chatHistory(Long userId, Long peerId);
    ConversationListResp conversations(Long userId);
}
