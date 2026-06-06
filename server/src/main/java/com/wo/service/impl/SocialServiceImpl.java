package com.wo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wo.common.exception.BusinessException;
import com.wo.dto.response.ConversationListResp;
import com.wo.dto.response.FollowListResp;
import com.wo.dto.response.MessageListResp;
import com.wo.entity.*;
import com.wo.mapper.*;
import com.wo.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final UserFollowMapper followMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MM-dd HH:mm");

    @Override
    @Transactional
    public void follow(Long userId, Long targetId) {
        if (userId.equals(targetId)) throw new BusinessException("不能关注自己");
        User target = userMapper.selectById(targetId);
        if (target == null) throw new BusinessException("用户不存在");
        UserFollow exist = followMapper.selectOne(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getFollowerId, userId).eq(UserFollow::getFolloweeId, targetId));
        if (exist != null) throw new BusinessException("已关注");
        UserFollow uf = new UserFollow();
        uf.setFollowerId(userId); uf.setFolloweeId(targetId);
        followMapper.insert(uf);
        userMapper.incrementFollowCount(userId);
        userMapper.incrementFansCount(targetId);
    }

    @Override
    @Transactional
    public void unfollow(Long userId, Long targetId) {
        UserFollow exist = followMapper.selectOne(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getFollowerId, userId).eq(UserFollow::getFolloweeId, targetId));
        if (exist == null) throw new BusinessException("未关注");
        followMapper.deleteById(exist.getId());
        userMapper.decrementFollowCount(userId);
        userMapper.decrementFansCount(targetId);
    }

    @Override
    public FollowListResp followers(Long userId, Long currentUserId) {
        List<UserFollow> records = followMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getFolloweeId, userId).orderByDesc(UserFollow::getCreatedAt));
        return buildFollowList(records, true, currentUserId);
    }

    @Override
    public FollowListResp following(Long userId, Long currentUserId) {
        List<UserFollow> records = followMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getFollowerId, userId).orderByDesc(UserFollow::getCreatedAt));
        return buildFollowList(records, false, currentUserId);
    }

    private FollowListResp buildFollowList(List<UserFollow> records, boolean isFollowers, Long currentUserId) {
        Set<Long> userIds = new HashSet<>();
        records.forEach(r -> userIds.add(isFollowers ? r.getFollowerId() : r.getFolloweeId()));
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) userMapper.selectBatchIds(userIds.stream().toList()).forEach(u -> userMap.put(u.getId(), u));

        // check which ones current user follows
        Set<Long> myFollowees = Collections.emptySet();
        if (currentUserId != null && !userIds.isEmpty()) {
            myFollowees = followMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                    .eq(UserFollow::getFollowerId, currentUserId).in(UserFollow::getFolloweeId, userIds))
                    .stream().map(UserFollow::getFolloweeId).collect(Collectors.toSet());
        }
        final Set<Long> finalFollowees = myFollowees;
        List<FollowListResp.Item> items = records.stream().map(r -> {
            Long uid = isFollowers ? r.getFollowerId() : r.getFolloweeId();
            User u = userMap.get(uid);
            if (u == null) return null;
            return FollowListResp.Item.builder()
                    .id(u.getId()).nickname(u.getNickname()).avatar(u.getAvatar()).bio(u.getBio())
                    .postCount(u.getPostCount()).fansCount(u.getFansCount())
                    .isFollowed(finalFollowees.contains(u.getId()))
                    .createdAt(r.getCreatedAt().format(FMT)).build();
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return FollowListResp.builder().items(items).total((long) items.size()).build();
    }

    @Override
    public void sendMessage(Long fromUid, Long toUid, String content) {
        if (content == null || content.trim().isEmpty()) throw new BusinessException("消息不能为空");
        Message msg = new Message();
        msg.setFromUid(fromUid); msg.setToUid(toUid);
        msg.setContent(content.trim()); msg.setIsRead(0);
        messageMapper.insert(msg);
    }

    @Override
    public MessageListResp chatHistory(Long userId, Long peerId) {
        List<Message> msgs = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .and(w -> w.eq(Message::getFromUid, userId).eq(Message::getToUid, peerId))
                .or(w -> w.eq(Message::getFromUid, peerId).eq(Message::getToUid, userId))
                .orderByAsc(Message::getCreatedAt));
        // mark read
        msgs.stream().filter(m -> m.getToUid().equals(userId) && m.getIsRead() == 0)
                .forEach(m -> { m.setIsRead(1); messageMapper.updateById(m); });
        List<MessageListResp.Item> items = msgs.stream().map(m -> MessageListResp.Item.builder()
                .id(m.getId()).fromUid(m.getFromUid()).toUid(m.getToUid())
                .content(m.getContent()).isRead(m.getIsRead())
                .createdAt(m.getCreatedAt().format(FMT)).build()).collect(Collectors.toList());
        return MessageListResp.builder().items(items).build();
    }

    @Override
    public ConversationListResp conversations(Long userId) {
        List<Message> all = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .and(w -> w.eq(Message::getFromUid, userId).or().eq(Message::getToUid, userId))
                .orderByDesc(Message::getCreatedAt));
        Map<Long, List<Message>> grouped = new LinkedHashMap<>();
        for (Message m : all) {
            Long peerId = m.getFromUid().equals(userId) ? m.getToUid() : m.getFromUid();
            grouped.putIfAbsent(peerId, new ArrayList<>());
            grouped.get(peerId).add(m);
        }
        Set<Long> peerIds = grouped.keySet();
        Map<Long, User> userMap = new HashMap<>();
        if (!peerIds.isEmpty()) userMapper.selectBatchIds(peerIds.stream().toList()).forEach(u -> userMap.put(u.getId(), u));

        List<ConversationListResp.Item> items = grouped.entrySet().stream().map(e -> {
            Long peerId = e.getKey();
            List<Message> msgs = e.getValue();
            Message last = msgs.get(0);
            long unread = msgs.stream().filter(m -> m.getToUid().equals(userId) && m.getIsRead() == 0).count();
            User u = userMap.get(peerId);
            return ConversationListResp.Item.builder()
                    .userId(peerId).nickname(u != null ? u.getNickname() : "匿名")
                    .avatar(u != null ? u.getAvatar() : "")
                    .lastContent(last.getContent().length() > 30 ? last.getContent().substring(0, 30) + "..." : last.getContent())
                    .unreadCount((int) unread).lastTime(last.getCreatedAt().format(FMT)).build();
        }).collect(Collectors.toList());
        return ConversationListResp.builder().items(items).build();
    }
}
