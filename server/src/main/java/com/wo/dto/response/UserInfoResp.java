package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResp {
    private Long id;
    private String nickname;
    private String avatar;
    private String bio;
    private Integer postCount;
    private Integer likeCount;
    private Integer fansCount;
    private Integer followCount;
    private String createdAt;
}
