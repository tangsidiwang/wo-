package com.wo.service;

import com.wo.dto.request.PostCreateReq;
import com.wo.dto.response.PostDetailResp;
import com.wo.dto.response.PostListResp;

public interface PostService {
    PostListResp list(int page, int size, String sort, Integer categoryId, String keyword, Long userId);
    PostListResp listByUser(int page, int size, Long userId);
    PostListResp listFavorites(int page, int size, Long userId);
    PostDetailResp detail(Long postId, Long currentUserId);
    PostDetailResp create(Long userId, PostCreateReq req);
    PostDetailResp update(Long userId, Long postId, PostCreateReq req);
    void delete(Long userId, Long postId);
    void toggleLike(Long userId, Long postId);
    void toggleFavorite(Long userId, Long postId);
}
