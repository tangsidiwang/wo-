package com.wo.service;

import com.wo.dto.request.PostCreateReq;
import com.wo.dto.response.PostDetailResp;
import com.wo.dto.response.PostListResp;
import com.wo.entity.Post;
import com.wo.mapper.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class PostServiceTest {

    @Autowired private PostService postService;
    @Autowired private PostMapper postMapper;
    @Autowired private PostLikeMapper postLikeMapper;
    @Autowired private PostFavoriteMapper postFavoriteMapper;

    private PostCreateReq buildReq() {
        PostCreateReq r = new PostCreateReq();
        r.setTitle("Hello World");
        r.setContent("<p>test</p>");
        r.setCategoryId(1);
        return r;
    }

    @Test @DisplayName("should create post and return detail with author")
    void shouldCreatePost() {
        PostDetailResp p = postService.create(1L, buildReq());
        assertThat(p.getId()).isNotNull();
        assertThat(p.getTitle()).isEqualTo("Hello World");
        assertThat(p.getAuthor().getNickname()).isEqualTo("Alice");
        assertThat(p.getCategoryName()).isEqualTo("Test Game");
    }

    @Test @DisplayName("should list posts with pagination")
    void shouldListPosts() {
        postService.create(1L, buildReq());
        postService.create(2L, buildReq());
        PostListResp list = postService.list(1, 10, "latest", null, null, null);
        assertThat(list.getItems()).hasSize(2);
        assertThat(list.getTotal()).isEqualTo(2L);
    }

    @Test @DisplayName("should list posts by category filter")
    void shouldFilterByCategory() {
        postService.create(1L, buildReq());
        PostCreateReq r2 = buildReq(); r2.setCategoryId(99);
        postService.create(2L, r2);
        PostListResp list = postService.list(1, 10, "latest", 1, null, null);
        assertThat(list.getItems()).hasSize(1);
    }

    @Test @DisplayName("should list user posts via listByUser")
    void shouldListByUser() {
        postService.create(1L, buildReq());
        postService.create(2L, buildReq());
        PostListResp list = postService.listByUser(1, 5, 1L);
        assertThat(list.getItems()).hasSize(1);
    }

    @Test @DisplayName("should toggle like on and off")
    void shouldToggleLike() {
        PostDetailResp p = postService.create(2L, buildReq());
        postService.toggleLike(1L, p.getId());
        PostDetailResp d = postService.detail(p.getId(), 1L);
        assertThat(d.getLikeCount()).isEqualTo(1);
        assertThat(d.getIsLiked()).isTrue();

        postService.toggleLike(1L, p.getId());
        d = postService.detail(p.getId(), 1L);
        assertThat(d.getLikeCount()).isEqualTo(0);
        assertThat(d.getIsLiked()).isFalse();
    }

    @Test @DisplayName("should toggle favorite on and off")
    void shouldToggleFavorite() {
        PostDetailResp p = postService.create(2L, buildReq());
        postService.toggleFavorite(1L, p.getId());
        PostListResp favs = postService.listFavorites(1, 10, 1L);
        assertThat(favs.getItems()).hasSize(1);

        postService.toggleFavorite(1L, p.getId());
        favs = postService.listFavorites(1, 10, 1L);
        assertThat(favs.getItems()).hasSize(0);
    }

    @Test @DisplayName("should delete post (soft delete)")
    void shouldSoftDelete() {
        PostDetailResp p = postService.create(1L, buildReq());
        postService.delete(1L, p.getId());
        Post post = postMapper.selectById(p.getId());
        assertThat(post.getStatus()).isNotEqualTo(1);
    }

    @Test @DisplayName("should not allow unauthorized delete")
    void shouldBlockUnauthDelete() {
        PostDetailResp p = postService.create(1L, buildReq());
        assertThatThrownBy(() -> postService.delete(2L, p.getId()))
                .hasMessageContaining("无权删除");
    }

    @Test @DisplayName("should search posts by keyword")
    void shouldSearchByKeyword() {
        PostCreateReq r = buildReq(); r.setTitle("hello world");
        postService.create(1L, r);
        PostCreateReq r2 = buildReq(); r2.setTitle("zzz");
        postService.create(1L, r2);
        PostListResp list = postService.list(1, 10, "latest", null, "hello", null);
        assertThat(list.getItems()).hasSize(1);
    }
}
