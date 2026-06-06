package com.wo.service;

import com.wo.dto.request.GuideCreateReq;
import com.wo.dto.response.GuideDetailResp;
import com.wo.dto.response.GuideListResp;

public interface GuideService {
    GuideListResp list(int page, int size, Integer gameId, Long userId);
    GuideDetailResp detail(Long id, Long currentUserId);
    GuideDetailResp create(Long userId, GuideCreateReq req);
    void delete(Long userId, Long guideId);
    void toggleFavorite(Long userId, Long guideId);
}
