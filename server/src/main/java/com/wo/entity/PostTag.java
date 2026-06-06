package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("post_tags")
public class PostTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Integer tagId;
}
