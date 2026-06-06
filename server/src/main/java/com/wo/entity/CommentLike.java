package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment_likes")
public class CommentLike {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long commentId;
    private LocalDateTime createdAt;
}
