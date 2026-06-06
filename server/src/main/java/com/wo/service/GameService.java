package com.wo.service;

import com.wo.dto.response.GameCategoryResp;

import java.util.List;

public interface GameService {
    List<GameCategoryResp> listCategories();
}
