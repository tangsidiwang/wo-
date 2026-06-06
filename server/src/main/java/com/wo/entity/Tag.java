package com.wo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tags")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer postCount;
    private Integer status;
}
