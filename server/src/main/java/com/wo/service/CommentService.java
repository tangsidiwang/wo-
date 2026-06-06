package com.wo.service;

import com.wo.dto.request.CommentCreateReq;
import com.wo.dto.response.CommentListResp;

public interface CommentService {
    CommentListResp list(Long postId, int page, int size, Long currentUserId);
    CommentListResp.Item create(Long postId, Long userId, CommentCreateReq req);
    void delete(Long commentId, Long userId);
    void toggleLike(Long commentId, Long userId);
}
