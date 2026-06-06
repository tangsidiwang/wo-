package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String coverUrl;
    private String summary;
    private String content;
    private String category;
    private String source;
    private String sourceUrl;
    private Integer viewCount;
    private Integer isTop;
    private Integer status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
}
