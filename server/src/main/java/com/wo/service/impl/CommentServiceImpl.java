package com.wo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wo.common.exception.BusinessException;
import com.wo.dto.request.CommentCreateReq;
import com.wo.dto.response.CommentListResp;
import com.wo.entity.Comment;
import com.wo.entity.CommentLike;
import com.wo.entity.Post;
import com.wo.entity.User;
import com.wo.mapper.CommentLikeMapper;
import com.wo.mapper.CommentMapper;
import com.wo.mapper.PostMapper;
import com.wo.mapper.UserMapper;
import com.wo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public CommentListResp list(Long postId, int page, int size, Long currentUserId) {
        // 查询一级评论
        var wrapper = new LambdaQueryWrapper<Comment>()
                .eq(Comment::getPostId, postId)
                .eq(Comment::getParentId, 0L)
                .eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreatedAt);
        var pageResult = commentMapper.selectPage(Page.of(page, size), wrapper);
        List<Comment> topComments = pageResult.getRecords();

        // 查所有一级评论的ID
        List<Long> topIds = topComments.stream().map(Comment::getId).toList();

        // 查子评论
        List<Comment> allReplies = List.of();
        if (!topIds.isEmpty()) {
            allReplies = commentMapper.selectList(
                    new LambdaQueryWrapper<Comment>()
                            .in(Comment::getParentId, topIds)
                            .eq(Comment::getStatus, 1)
                            .orderByAsc(Comment::getCreatedAt)
            );
        }

        // 收集所有用户ID
        Set<Long> userIds = new HashSet<>();
        topComments.forEach(c -> userIds.add(c.getUserId()));
        allReplies.forEach(c -> userIds.add(c.getUserId()));

        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            userMapper.selectBatchIds(userIds.stream().toList())
                    .forEach(u -> userMap.put(u.getId(), u));
        }

        // 收集所有评论ID查点赞状态
        Set<Long> allCommentIds = new HashSet<>(topIds);
        allReplies.forEach(c -> allCommentIds.add(c.getId()));
        Set<Long> likedSet = Collections.emptySet();
        if (currentUserId != null && !allCommentIds.isEmpty()) {
            likedSet = commentLikeMapper.selectList(
                    new LambdaQueryWrapper<CommentLike>()
                            .eq(CommentLike::getUserId, currentUserId)
                            .in(CommentLike::getCommentId, allCommentIds)
            ).stream().map(CommentLike::getCommentId).collect(Collectors.toSet());
        }

        // 组装子评论按 parent_id 分组
        Map<Long, List<Comment>> replyMap = new HashMap<>();
        for (Comment r : allReplies) {
            replyMap.computeIfAbsent(r.getParentId(), k -> new ArrayList<>()).add(r);
        }

        final Set<Long> finalLiked = likedSet;
        List<CommentListResp.Item> items = topComments.stream()
                .map(c -> toItem(c, userMap, replyMap, finalLiked))
                .collect(Collectors.toList());

        return CommentListResp.builder()
                .items(items)
                .total(pageResult.getTotal())
                .page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages())
                .build();
    }

    @Override
    @Transactional
    public CommentListResp.Item create(Long postId, Long userId, CommentCreateReq req) {
        Post post = postMapper.selectById(postId);
        if (post == null || post.getStatus() != 1) throw new BusinessException("帖子不存在");

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(req.getContent());
        comment.setParentId(req.getParentId() != null ? req.getParentId() : 0L);
        comment.setReplyToUid(req.getReplyToUid() != null ? req.getReplyToUid() : 0L);
        comment.setStatus(1);
        commentMapper.insert(comment);

        // 更新计数
        post.setCommentCount(post.getCommentCount() + 1);
        postMapper.updateById(post);

        if (comment.getParentId() > 0) {
            Comment parent = commentMapper.selectById(comment.getParentId());
            if (parent != null) {
                parent.setReplyCount(parent.getReplyCount() + 1);
                commentMapper.updateById(parent);
            }
        }

        User user = userMapper.selectById(userId);
        return toItem(comment, Map.of(userId, user), Map.of(), Set.of());
    }

    @Override
    public void delete(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || !comment.getUserId().equals(userId))
            throw new BusinessException("无权删除");
        comment.setStatus(2);
        commentMapper.updateById(comment);
    }

    @Override
    @Transactional
    public void toggleLike(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new BusinessException("评论不存在");

        CommentLike exist = commentLikeMapper.selectOne(
                new LambdaQueryWrapper<CommentLike>()
                        .eq(CommentLike::getUserId, userId)
                        .eq(CommentLike::getCommentId, commentId));
        if (exist != null) {
            commentLikeMapper.deleteById(exist.getId());
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        } else {
            CommentLike cl = new CommentLike();
            cl.setUserId(userId);
            cl.setCommentId(commentId);
            commentLikeMapper.insert(cl);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        commentMapper.updateById(comment);
    }

    private CommentListResp.Item toItem(Comment c, Map<Long, User> userMap,
                                         Map<Long, List<Comment>> replyMap,
                                         Set<Long> likedSet) {
        User u = userMap.get(c.getUserId());
        List<Comment> replies = replyMap.getOrDefault(c.getId(), List.of());
        return CommentListResp.Item.builder()
                .id(c.getId())
                .userId(c.getUserId())
                .nickname(u != null ? u.getNickname() : "匿名")
                .avatar(u != null ? u.getAvatar() : "")
                .content(c.getContent())
                .likeCount(c.getLikeCount())
                .isLiked(likedSet.contains(c.getId()))
                .createdAt(c.getCreatedAt() != null ? c.getCreatedAt().format(FMT) : "")
                .replies(replies.stream()
                        .map(r -> toItem(r, userMap, Map.of(), likedSet))
                        .collect(Collectors.toList()))
                .build();
    }
}
