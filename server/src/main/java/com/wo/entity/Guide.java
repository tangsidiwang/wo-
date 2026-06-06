package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("guides")
public class Guide {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer gameId;
    private String title;
    private String coverUrl;
    private String summary;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer sectionCount;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
