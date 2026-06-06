package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("guide_sections")
public class GuideSection {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long guideId;
    private Integer sortOrder;
    private String subtitle;
    private String content;
    private String imageUrl;
}
