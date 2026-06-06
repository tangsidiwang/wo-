package com.wo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameCategoryResp {
    private Integer id;
    private String name;
    private String icon;
    private Integer postCount;
}
