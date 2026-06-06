package com.wo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResp {
    private String token;
    private UserInfoResp user;
}
