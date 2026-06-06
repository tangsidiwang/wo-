package com.wo.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wo.common.Result;
import com.wo.dto.response.LoginResp;
import com.wo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, String> body) {
        authService.register(body.get("username"), body.get("password"), body.get("nickname"));
        return Result.ok("注册成功");
    }

    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username != null && password != null) {
            return Result.ok(authService.loginByPassword(username, password));
        }
        return Result.ok(authService.loginByPhone(body.get("phone"), body.get("smsCode")));
    }

    @PostMapping("/send-sms")
    public Result<?> sendSms(@RequestBody Map<String, String> body) {
        authService.sendSms(body.get("phone"));
        return Result.ok("验证码已发送");
    }

    @PostMapping("/wechat-login")
    public Result<LoginResp> wechatLogin(@RequestBody Map<String, String> body) {
        return Result.ok(authService.loginByWechat(
                body.get("code"), body.get("nickname"), body.get("avatar")));
    }

    @PostMapping("/dev-login")
    public Result<LoginResp> devLogin(@RequestBody Map<String, String> body) {
        return Result.ok(authService.devLogin(body.getOrDefault("nickname", "")));
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.ok("已退出");
    }
}
