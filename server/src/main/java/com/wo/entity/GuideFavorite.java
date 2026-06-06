package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("guide_favorites")
public class GuideFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long guideId;
    private LocalDateTime createdAt;
}
