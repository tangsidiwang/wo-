package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String openid;
    private String unionid;
    private String phone;
    private String nickname;
    private String avatar;
    private String bio;
    private Integer gender;
    private Integer status;
    private String role;
    private Integer postCount;
    private Integer likeCount;
    private Integer fansCount;
    private Integer followCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
