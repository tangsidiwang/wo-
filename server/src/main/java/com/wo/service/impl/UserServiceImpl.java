package com.wo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wo.common.exception.BusinessException;
import com.wo.dto.response.UserInfoResp;
import com.wo.entity.User;
import com.wo.mapper.UserMapper;
import com.wo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserInfoResp getMyInfo(Long userId) {
        return getUserInfo(userId);
    }

    @Override
    public UserInfoResp getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException("用户不存在");
        }
        return toResp(user);
    }

    @Override
    public UserInfoResp updateProfile(Long userId, Map<String, Object> data) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (data.containsKey("nickname")) user.setNickname((String) data.get("nickname"));
        if (data.containsKey("avatar")) user.setAvatar((String) data.get("avatar"));
        if (data.containsKey("bio")) user.setBio((String) data.get("bio"));
        userMapper.updateById(user);
        return toResp(user);
    }

    @Override
    public User getOrCreateByOpenid(String openid, String nickname, String avatar) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getOpenid, openid)
        );
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname != null && !nickname.isEmpty() ? nickname : "游戏玩家");
            user.setAvatar(avatar != null ? avatar : "");
            userMapper.insert(user);
        }
        return user;
    }

    private UserInfoResp toResp(User user) {
        return UserInfoResp.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .postCount(user.getPostCount() != null ? user.getPostCount() : 0)
                .likeCount(user.getLikeCount() != null ? user.getLikeCount() : 0)
                .fansCount(user.getFansCount() != null ? user.getFansCount() : 0)
                .followCount(user.getFollowCount() != null ? user.getFollowCount() : 0)
                .createdAt(user.getCreatedAt() != null ?
                        user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "")
                .build();
    }
}
