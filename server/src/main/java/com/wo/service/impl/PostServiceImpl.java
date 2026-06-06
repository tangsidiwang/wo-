package com.wo.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wo.common.exception.BusinessException;
import com.wo.dto.request.PostCreateReq;
import com.wo.dto.response.PostDetailResp;
import com.wo.dto.response.PostDetailResp.ImageInfo;
import com.wo.dto.response.PostDetailResp.UserBrief;
import com.wo.dto.response.PostListResp;
import com.wo.entity.*;
import com.wo.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements com.wo.service.PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final GameCategoryMapper gameCategoryMapper;
    private final com.wo.mapper.PostImageMapper postImageMapper;
    private final com.wo.mapper.PostTagMapper postTagMapper;
    private final com.wo.mapper.TagMapper tagMapper;
    private final com.wo.mapper.PostLikeMapper postLikeMapper;
    private final com.wo.mapper.PostFavoriteMapper postFavoriteMapper;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public PostListResp list(int page, int size, String sort, Integer categoryId, String keyword, Long userId) {
        var wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, 1)
                .eq(categoryId != null && categoryId > 0, Post::getCategoryId, categoryId)
                .and(keyword != null && !keyword.isBlank(), w -> w
                        .like(Post::getTitle, keyword).or().like(Post::getContentText, keyword));

        if ("hot".equals(sort)) {
            wrapper.orderByDesc(Post::getLikeCount);
        } else {
            wrapper.orderByDesc(Post::getCreatedAt);
        }

        var pageResult = postMapper.selectPage(Page.of(page, size), wrapper);
        List<Post> posts = pageResult.getRecords();

        List<Long> userIds = posts.stream().map(Post::getUserId).distinct().toList();
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            userMapper.selectBatchIds(userIds).forEach(u -> userMap.put(u.getId(), u));
        }

        Map<Integer, GameCategory> catMap = new HashMap<>();
        List<Integer> catIds = posts.stream().map(Post::getCategoryId).filter(i -> i > 0).distinct().toList();
        if (!catIds.isEmpty()) {
            gameCategoryMapper.selectBatchIds(catIds).forEach(c -> catMap.put(c.getId(), c));
        }

        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<String>> thumbMap = new HashMap<>();
        if (!postIds.isEmpty()) {
            postImageMapper.selectList(new LambdaQueryWrapper<com.wo.entity.PostImage>()
                            .in(com.wo.entity.PostImage::getPostId, postIds)
                            .orderByAsc(com.wo.entity.PostImage::getSortOrder))
                    .forEach(img -> thumbMap.computeIfAbsent(img.getPostId(), k -> new ArrayList<>())
                            .add(img.getThumbUrl() != null && !img.getThumbUrl().isEmpty() ? img.getThumbUrl() : img.getUrl()));
        }

        Set<Long> likedSet = Collections.emptySet();
        Set<Long> favSet = Collections.emptySet();
        if (userId != null && !postIds.isEmpty()) {
            likedSet = postLikeMapper.selectList(new LambdaQueryWrapper<PostLike>()
                            .eq(PostLike::getUserId, userId).in(PostLike::getPostId, postIds))
                    .stream().map(PostLike::getPostId).collect(Collectors.toSet());
            favSet = postFavoriteMapper.selectList(new LambdaQueryWrapper<PostFavorite>()
                            .eq(PostFavorite::getUserId, userId).in(PostFavorite::getPostId, postIds))
                    .stream().map(PostFavorite::getPostId).collect(Collectors.toSet());
        }

        final Set<Long> finalLiked = likedSet;
        final Set<Long> finalFav = favSet;
        List<PostListResp.Item> items = posts.stream().map(p -> {
            User u = userMap.get(p.getUserId());
            GameCategory c = p.getCategoryId() != null && p.getCategoryId() > 0 ? catMap.get(p.getCategoryId()) : null;
            return PostListResp.Item.builder()
                    .id(p.getId()).userId(p.getUserId())
                    .authorNickname(u != null ? u.getNickname() : "匿名")
                    .authorAvatar(u != null ? u.getAvatar() : "")
                    .categoryName(c != null ? c.getName() : "")
                    .title(p.getTitle())
                    .contentText(p.getContentText() != null ? (p.getContentText().length() > 200 ? p.getContentText().substring(0, 200) + "..." : p.getContentText()) : "")
                    .coverUrl(p.getCoverUrl())
                    .imageThumbUrls(thumbMap.getOrDefault(p.getId(), List.of()))
                    .viewCount(p.getViewCount()).likeCount(p.getLikeCount()).commentCount(p.getCommentCount())
                    .isLiked(finalLiked.contains(p.getId())).isFavorited(finalFav.contains(p.getId()))
                    .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().format(FMT) : "")
                    .build();
        }).collect(Collectors.toList());

        return PostListResp.builder()
                .items(items).total(pageResult.getTotal()).page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
    }

    @Override
    public PostListResp listByUser(int page, int size, Long userId) {
        var wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, 1)
                .eq(Post::getUserId, userId)
                .orderByDesc(Post::getCreatedAt);
        var pageResult = postMapper.selectPage(Page.of(page, size), wrapper);
        List<Post> posts = pageResult.getRecords();
        User author = userMapper.selectById(userId);
        List<Integer> catIds = posts.stream().map(Post::getCategoryId).filter(i -> i > 0).distinct().toList();
        Map<Integer, GameCategory> catMap = new HashMap<>();
        if (!catIds.isEmpty()) gameCategoryMapper.selectBatchIds(catIds).forEach(c -> catMap.put(c.getId(), c));
        List<PostListResp.Item> items = posts.stream().map(p -> {
            GameCategory gc = catMap.get(p.getCategoryId());
            return PostListResp.Item.builder()
                    .id(p.getId()).title(p.getTitle())
                    .contentText(p.getContentText() != null ? (p.getContentText().length() > 200 ? p.getContentText().substring(0, 200) + "..." : p.getContentText()) : "")
                    .authorNickname(author != null ? author.getNickname() : "匿名")
                    .authorAvatar(author != null ? author.getAvatar() : "")
                    .categoryName(gc != null ? gc.getName() : "").viewCount(p.getViewCount())
                    .likeCount(p.getLikeCount()).commentCount(p.getCommentCount())
                    .isLiked(false).isFavorited(false)
                    .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().format(FMT) : "").build();
        }).collect(Collectors.toList());
        return PostListResp.builder().items(items).total(pageResult.getTotal()).page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
    }

    @Override
    public PostListResp listFavorites(int page, int size, Long userId) {
        var pageResult = postFavoriteMapper.selectPage(Page.of(page, size),
                new LambdaQueryWrapper<PostFavorite>()
                        .eq(PostFavorite::getUserId, userId)
                        .orderByDesc(PostFavorite::getCreatedAt));
        long total = pageResult.getTotal();
        List<Long> postIds = pageResult.getRecords().stream().map(PostFavorite::getPostId).toList();
        if (postIds.isEmpty()) {
            return PostListResp.builder().items(List.of()).total(total).page(page)
                    .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
        }
        List<Post> posts = postMapper.selectBatchIds(postIds).stream()
                .filter(p -> p.getStatus() == 1).toList();
        List<Long> authorIds = posts.stream().map(Post::getUserId).distinct().toList();
        Map<Long, User> userMap = new HashMap<>();
        if (!authorIds.isEmpty()) userMapper.selectBatchIds(authorIds).forEach(u -> userMap.put(u.getId(), u));
        List<PostListResp.Item> items = posts.stream().map(p -> {
            User u = userMap.get(p.getUserId());
            return PostListResp.Item.builder()
                    .id(p.getId()).title(p.getTitle())
                    .contentText(p.getContentText() != null ? (p.getContentText().length() > 200 ? p.getContentText().substring(0, 200) + "..." : p.getContentText()) : "")
                    .authorNickname(u != null ? u.getNickname() : "匿名")
                    .authorAvatar(u != null ? u.getAvatar() : "")
                    .categoryName("").viewCount(p.getViewCount())
                    .likeCount(p.getLikeCount()).commentCount(p.getCommentCount())
                    .isLiked(false).isFavorited(true)
                    .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().format(FMT) : "").build();
        }).collect(Collectors.toList());
        return PostListResp.builder().items(items).total(total).page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
    }

    @Override
    public PostDetailResp detail(Long postId, Long currentUserId) {
        Post post = postMapper.selectById(postId);
        if (post == null || post.getStatus() != 1) throw new BusinessException("帖子不存在或已删除");

        // Atomic view count
        postMapper.incrementViewCount(postId);
        post.setViewCount(post.getViewCount() + 1);

        User author = userMapper.selectById(post.getUserId());
        GameCategory cat = post.getCategoryId() != null ? gameCategoryMapper.selectById(post.getCategoryId()) : null;

        List<com.wo.entity.PostImage> images = postImageMapper.selectList(
                new LambdaQueryWrapper<com.wo.entity.PostImage>()
                        .eq(com.wo.entity.PostImage::getPostId, postId)
                        .orderByAsc(com.wo.entity.PostImage::getSortOrder));

        List<PostTag> ptags = postTagMapper.selectList(
                new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId));
        List<String> tagNames = List.of();
        if (!ptags.isEmpty()) {
            List<Integer> tagIds = ptags.stream().map(PostTag::getTagId).toList();
            tagNames = tagMapper.selectBatchIds(tagIds).stream().map(Tag::getName).toList();
        }

        boolean liked = false, faved = false;
        if (currentUserId != null) {
            liked = postLikeMapper.exists(new LambdaQueryWrapper<PostLike>()
                    .eq(PostLike::getUserId, currentUserId).eq(PostLike::getPostId, postId));
            faved = postFavoriteMapper.exists(new LambdaQueryWrapper<PostFavorite>()
                    .eq(PostFavorite::getUserId, currentUserId).eq(PostFavorite::getPostId, postId));
        }

        return PostDetailResp.builder()
                .id(post.getId()).userId(post.getUserId())
                .author(UserBrief.builder().id(author != null ? author.getId() : 0L)
                        .nickname(author != null ? author.getNickname() : "匿名")
                        .avatar(author != null ? author.getAvatar() : "").build())
                .categoryName(cat != null ? cat.getName() : "")
                .title(post.getTitle()).content(post.getContent())
                .contentText(post.getContentText()).coverUrl(post.getCoverUrl())
                .images(images.stream().map(img -> ImageInfo.builder()
                        .id(img.getId()).url(img.getUrl()).thumbUrl(img.getThumbUrl())
                        .width(img.getWidth()).height(img.getHeight()).build()).toList())
                .tags(tagNames)
                .viewCount(post.getViewCount()).likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount()).favoriteCount(post.getFavoriteCount())
                .isLiked(liked).isFavorited(faved)
                .createdAt(post.getCreatedAt() != null ? post.getCreatedAt().format(FMT) : "")
                .build();
    }

    @Override
    @Transactional
    public PostDetailResp create(Long userId, PostCreateReq req) {
        Post post = new Post();
        post.setUserId(userId);
        post.setCategoryId(req.getCategoryId() != null ? req.getCategoryId() : 0);
        post.setTitle(req.getTitle());
        post.setContent(req.getContent() != null ? req.getContent() : "");
        // 简单 html 转纯文本
        post.setContentText(req.getContent() != null ? req.getContent().replaceAll("<[^>]+>", "").trim() : "");
        post.setCoverUrl(req.getCoverUrl() != null ? req.getCoverUrl() : "");
        post.setStatus(1);
        postMapper.insert(post);

        // 处理标签
        if (req.getTagNames() != null && !req.getTagNames().isEmpty()) {
            for (String tagName : req.getTagNames()) {
                Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setStatus(1);
                    tagMapper.insert(tag);
                }
                PostTag pt = new PostTag();
                pt.setPostId(post.getId());
                pt.setTagId(tag.getId());
                postTagMapper.insert(pt);
            }
        }

        // 处理图片
        if (req.getImageUrls() != null && !req.getImageUrls().isEmpty()) {
            for (int i = 0; i < req.getImageUrls().size(); i++) {
                com.wo.entity.PostImage pi = new com.wo.entity.PostImage();
                pi.setPostId(post.getId());
                pi.setUrl(req.getImageUrls().get(i));
                pi.setSortOrder(i);
                postImageMapper.insert(pi);
            }
        }

        // Atomic increment
        if (post.getCategoryId() > 0) gameCategoryMapper.incrementPostCount(post.getCategoryId());
        userMapper.incrementPostCount(userId);

        return detail(post.getId(), userId);
    }

    @Override
    public PostDetailResp update(Long userId, Long postId, PostCreateReq req) {
        Post post = postMapper.selectById(postId);
        if (post == null || !post.getUserId().equals(userId)) throw new BusinessException("无权编辑");
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setContentText(req.getContent().replaceAll("<[^>]+>", "").trim());
        if (req.getCategoryId() != null) post.setCategoryId(req.getCategoryId());
        if (req.getCoverUrl() != null) post.setCoverUrl(req.getCoverUrl());
        postMapper.updateById(post);
        return detail(postId, userId);
    }

    @Override
    public void delete(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null || !post.getUserId().equals(userId)) throw new BusinessException("无权删除");
        post.setStatus(3);
        postMapper.updateById(post);
    }

    @Override
    @Transactional
    public void toggleLike(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) throw new BusinessException("帖子不存在");

        PostLike exist = postLikeMapper.selectOne(new LambdaQueryWrapper<PostLike>()
                .eq(PostLike::getUserId, userId).eq(PostLike::getPostId, postId));
        if (exist != null) {
            postLikeMapper.deleteById(exist.getId());
            postMapper.decrementLikeCount(postId);
        } else {
            PostLike pl = new PostLike(); pl.setUserId(userId); pl.setPostId(postId);
            postLikeMapper.insert(pl);
            postMapper.incrementLikeCount(postId);
        }
    }

    @Override
    @Transactional
    public void toggleFavorite(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) throw new BusinessException("帖子不存在");

        PostFavorite exist = postFavoriteMapper.selectOne(new LambdaQueryWrapper<PostFavorite>()
                .eq(PostFavorite::getUserId, userId).eq(PostFavorite::getPostId, postId));
        if (exist != null) {
            postFavoriteMapper.deleteById(exist.getId());
            postMapper.decrementFavoriteCount(postId);
        } else {
            PostFavorite pf = new PostFavorite(); pf.setUserId(userId); pf.setPostId(postId);
            postFavoriteMapper.insert(pf);
            postMapper.incrementFavoriteCount(postId);
        }
    }
}
