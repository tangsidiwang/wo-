package com.wo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WechatLoginReq {
    @NotBlank(message = "code不能为空")
    private String code;

    private String nickname;
    private String avatar;
}
