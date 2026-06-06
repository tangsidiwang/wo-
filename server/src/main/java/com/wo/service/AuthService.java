package com.wo.service;

import com.wo.dto.response.LoginResp;

public interface AuthService {
    void sendSms(String phone);
    LoginResp loginByPhone(String phone, String smsCode);
    LoginResp loginByWechat(String code, String nickname, String avatar);
    LoginResp devLogin(String nickname);
    void register(String username, String password, String nickname);
    LoginResp loginByPassword(String username, String password);
}
