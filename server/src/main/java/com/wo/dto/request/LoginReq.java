package com.wo.dto.request;

import lombok.Data;

@Data
public class LoginReq {
    private String phone;
    private String smsCode;
}
