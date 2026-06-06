package com.wo.service;

import com.wo.dto.response.FollowListResp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class SocialServiceTest {

    @Autowired private SocialService socialService;

    @Test @DisplayName("should follow user")
    void shouldFollow() {
        socialService.follow(1L, 2L);
        FollowListResp following = socialService.following(1L, 1L);
        assertThat(following.getItems()).hasSize(1);
        assertThat(following.getItems().get(0).getId()).isEqualTo(2L);
    }

    @Test @DisplayName("should unfollow user")
    void shouldUnfollow() {
        socialService.follow(1L, 2L);
        socialService.unfollow(1L, 2L);
        FollowListResp following = socialService.following(1L, 1L);
        assertThat(following.getItems()).hasSize(0);
    }

    @Test @DisplayName("should return followers and following")
    void shouldListFollowers() {
        socialService.follow(1L, 2L);
        FollowListResp followers = socialService.followers(2L, 1L);
        assertThat(followers.getItems()).hasSize(1);
        assertThat(followers.getItems().get(0).getId()).isEqualTo(1L);
    }

    @Test @DisplayName("should send and retrieve messages")
    void shouldSendMessage() {
        socialService.sendMessage(1L, 2L, "hello");
        socialService.sendMessage(2L, 1L, "hi back");
        var msgs = socialService.chatHistory(1L, 2L);
        assertThat(msgs.getItems()).hasSize(2);
        assertThat(msgs.getItems().get(0).getContent()).isEqualTo("hello");
    }

    @Test @DisplayName("should list conversations with unread count")
    void shouldListConversations() {
        socialService.sendMessage(2L, 1L, "msg from bob");
        var convs = socialService.conversations(1L);
        assertThat(convs.getItems()).hasSize(1);
        assertThat(convs.getItems().get(0).getUnreadCount()).isEqualTo(1);
    }

    @Test @DisplayName("should not follow self")
    void shouldBlockSelfFollow() {
        assertThatThrownBy(() -> socialService.follow(1L, 1L))
                .hasMessageContaining("不能关注自己");
    }

    @Test @DisplayName("should not double follow")
    void shouldBlockDoubleFollow() {
        socialService.follow(1L, 2L);
        assertThatThrownBy(() -> socialService.follow(1L, 2L))
                .hasMessageContaining("已关注");
    }
}
