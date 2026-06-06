package com.wo.service;

import com.wo.dto.request.CommentCreateReq;
import com.wo.dto.response.CommentListResp;
import com.wo.dto.response.PostDetailResp;
import com.wo.dto.request.PostCreateReq;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class CommentServiceTest {

    @Autowired private CommentService commentService;
    @Autowired private PostService postService;
    private Long postId;

    @BeforeEach
    void setUp() {
        PostCreateReq r = new PostCreateReq(); r.setTitle("test"); r.setContent("<p>x</p>");
        PostDetailResp p = postService.create(1L, r);
        postId = p.getId();
    }

    @Test @DisplayName("should create top-level comment")
    void shouldCreateComment() {
        CommentCreateReq req = new CommentCreateReq(); req.setContent("great!");
        CommentListResp.Item c = commentService.create(postId, 2L, req);
        assertThat(c.getId()).isNotNull();
        assertThat(c.getNickname()).isEqualTo("Bob");
        assertThat(c.getContent()).isEqualTo("great!");
    }

    @Test @DisplayName("should create threaded reply")
    void shouldCreateReply() {
        CommentCreateReq parent = new CommentCreateReq(); parent.setContent("hello");
        CommentListResp.Item p = commentService.create(postId, 1L, parent);

        CommentCreateReq reply = new CommentCreateReq();
        reply.setContent("reply"); reply.setParentId(p.getId()); reply.setReplyToUid(1L);
        CommentListResp.Item r = commentService.create(postId, 2L, reply);

        CommentListResp list = commentService.list(postId, 1, 10, null);
        assertThat(list.getItems()).hasSize(1);
        assertThat(list.getItems().get(0).getReplies()).hasSize(1);
    }

    @Test @DisplayName("should toggle comment like")
    void shouldToggleCommentLike() {
        CommentCreateReq req = new CommentCreateReq(); req.setContent("nice");
        CommentListResp.Item c = commentService.create(postId, 2L, req);
        commentService.toggleLike(c.getId(), 1L);
        CommentListResp list = commentService.list(postId, 1, 10, 1L);
        assertThat(list.getItems().get(0).getIsLiked()).isTrue();
        assertThat(list.getItems().get(0).getLikeCount()).isEqualTo(1);

        commentService.toggleLike(c.getId(), 1L);
        list = commentService.list(postId, 1, 10, 1L);
        assertThat(list.getItems().get(0).getIsLiked()).isFalse();
    }

    @Test @DisplayName("should soft delete comment")
    void shouldDeleteComment() {
        CommentCreateReq req = new CommentCreateReq(); req.setContent("bad");
        CommentListResp.Item c = commentService.create(postId, 1L, req);
        commentService.delete(c.getId(), 1L);
        CommentListResp list = commentService.list(postId, 1, 10, null);
        assertThat(list.getItems()).hasSize(0);
    }
}
