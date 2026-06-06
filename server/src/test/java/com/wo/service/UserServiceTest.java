package com.wo.service;

import com.wo.dto.response.UserInfoResp;
import com.wo.entity.User;
import com.wo.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserMapper userMapper;

    @Test @DisplayName("getUserInfo should return user by id")
    void shouldGetUserInfo() {
        UserInfoResp u = userService.getUserInfo(1L);
        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getNickname()).isEqualTo("Alice");
    }

    @Test @DisplayName("getUserInfo should throw when user not found")
    void shouldThrowWhenUserNotFound() {
        assertThatThrownBy(() -> userService.getUserInfo(999L))
                .hasMessageContaining("用户不存在");
    }

    @Test @DisplayName("getOrCreateByOpenid should create new user when not exist")
    void shouldCreateByOpenid() {
        User u = userService.getOrCreateByOpenid("new:test", "NewUser", "/a.jpg");
        assertThat(u.getId()).isNotNull();
        assertThat(u.getNickname()).isEqualTo("NewUser");
        assertThat(userMapper.selectCount(null)).isEqualTo(3L);
    }

    @Test @DisplayName("getOrCreateByOpenid should return existing user")
    void shouldReturnExisting() {
        User u1 = userService.getOrCreateByOpenid("wx:alice", "Ignored", "");
        User u2 = userService.getOrCreateByOpenid("wx:alice", "Ignored2", "");
        assertThat(u2.getId()).isEqualTo(u1.getId());
    }

    @Test @DisplayName("updateProfile should modify nickname/bio/avatar")
    void shouldUpdateProfile() {
        UserInfoResp r = userService.updateProfile(1L, java.util.Map.of("nickname", "Alice2", "bio", "hello"));
        assertThat(r.getNickname()).isEqualTo("Alice2");
        assertThat(r.getBio()).isEqualTo("hello");
    }

    @Test @DisplayName("getMyInfo is alias of getUserInfo")
    void shouldGetMyInfo() {
        UserInfoResp r = userService.getMyInfo(2L);
        assertThat(r.getNickname()).isEqualTo("Bob");
    }
}
