package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("post_images")
public class PostImage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private String url;
    private String thumbUrl;
    private Integer width;
    private Integer height;
    private Integer sortOrder;
}
