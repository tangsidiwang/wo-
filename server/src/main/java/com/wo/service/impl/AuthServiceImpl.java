package com.wo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.wo.common.exception.BusinessException;
import com.wo.dto.response.LoginResp;
import com.wo.entity.User;
import com.wo.mapper.UserMapper;
import com.wo.service.AuthService;
import com.wo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final Map<String, String> smsCache = new ConcurrentHashMap<>();

    @Override
    public void register(String username, String password, String nickname) {
        if (username == null || username.trim().length() < 3) {
            throw new BusinessException("账号至少3位");
        }
        if (password == null || password.trim().length() < 6) {
            throw new BusinessException("密码至少6位");
        }
        User exist = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username.trim())
        );
        if (exist != null) {
            throw new BusinessException("账号已被注册");
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = username.trim();
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setNickname(nickname.trim());
        user.setOpenid("pwd:" + username.trim());
        userMapper.insert(user);
        log.info("User registered: {} -> id={}", username, user.getId());
    }

    @Override
    public LoginResp loginByPassword(String username, String password) {
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username.trim())
        );
        if (user == null) {
            throw new BusinessException("账号不存在");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BusinessException("该账号未设置密码，请用快捷登录");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        return new LoginResp(token, userService.getMyInfo(user.getId()));
    }

    @Override
    public void sendSms(String phone) {
        String code = String.format("%06d", (int) (Math.random() * 1000000));
        smsCache.put(phone, code);
        log.info("SMS code for {}: {}", phone, code);
    }

    @Override
    public LoginResp loginByPhone(String phone, String smsCode) {
        String cached = smsCache.get(phone);
        if (cached == null || !cached.equals(smsCode)) {
            throw new BusinessException("验证码错误或已过期");
        }
        smsCache.remove(phone);
        User user = userService.getOrCreateByOpenid("phone:" + phone, "手机用户" + phone.substring(7), "");
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        return new LoginResp(token, userService.getMyInfo(user.getId()));
    }

    @Override
    public LoginResp devLogin(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "游戏玩家";
        }
        String openid = "dev:" + System.currentTimeMillis();
        User user = userService.getOrCreateByOpenid(openid, nickname.trim(), "");
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        log.info("Dev login: {} -> id={}, token={}", nickname, user.getId(), token);
        return new LoginResp(token, userService.getMyInfo(user.getId()));
    }

    @Override
    public LoginResp loginByWechat(String code, String nickname, String avatar) {
        String openid = "wechat:" + (code.hashCode() & 0x7FFFFFFF);
        User user = userService.getOrCreateByOpenid(openid, nickname, avatar);
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        return new LoginResp(token, userService.getMyInfo(user.getId()));
    }
}
