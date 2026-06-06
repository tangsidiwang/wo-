package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("game_categories")
public class GameCategory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String icon;
    private Integer sortOrder;
    private Integer postCount;
    private Integer status;
    private LocalDateTime createdAt;
}
