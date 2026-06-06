package com.wo.service;

import com.wo.dto.response.UserInfoResp;
import com.wo.entity.User;

import java.util.Map;

public interface UserService {
    UserInfoResp getMyInfo(Long userId);
    UserInfoResp getUserInfo(Long userId);
    UserInfoResp updateProfile(Long userId, Map<String, Object> data);
    User getOrCreateByOpenid(String openid, String nickname, String avatar);
}
