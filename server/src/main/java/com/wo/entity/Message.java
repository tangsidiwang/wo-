package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("messages")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fromUid;
    private Long toUid;
    private String content;
    private Integer isRead;
    private LocalDateTime createdAt;
}
