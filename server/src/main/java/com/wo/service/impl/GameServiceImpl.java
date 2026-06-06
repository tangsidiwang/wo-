package com.wo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wo.dto.response.GameCategoryResp;
import com.wo.entity.GameCategory;
import com.wo.mapper.GameCategoryMapper;
import com.wo.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameCategoryMapper gameCategoryMapper;

    @Override
    public List<GameCategoryResp> listCategories() {
        List<GameCategory> list = gameCategoryMapper.selectList(
                new LambdaQueryWrapper<GameCategory>()
                        .eq(GameCategory::getStatus, 1)
                        .orderByAsc(GameCategory::getSortOrder)
        );
        return list.stream().map(g -> GameCategoryResp.builder()
                .id(g.getId())
                .name(g.getName())
                .icon(g.getIcon())
                .postCount(g.getPostCount() != null ? g.getPostCount() : 0)
                .build()
        ).collect(Collectors.toList());
    }
}
