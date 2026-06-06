package com.wo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wo.common.exception.BusinessException;
import com.wo.dto.request.GuideCreateReq;
import com.wo.dto.response.GuideDetailResp;
import com.wo.dto.response.GuideListResp;
import com.wo.entity.*;
import com.wo.mapper.*;
import com.wo.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    private final GuideMapper guideMapper;
    private final GuideSectionMapper sectionMapper;
    private final GuideFavoriteMapper guideFavoriteMapper;
    private final UserMapper userMapper;
    private final GameCategoryMapper gameCategoryMapper;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public GuideListResp list(int page, int size, Integer gameId, Long userId) {
        var wrapper = new LambdaQueryWrapper<Guide>()
                .eq(Guide::getStatus, 1)
                .eq(gameId != null && gameId > 0, Guide::getGameId, gameId)
                .eq(userId != null, Guide::getUserId, userId)
                .orderByDesc(Guide::getCreatedAt);
        var pageResult = guideMapper.selectPage(Page.of(page, size), wrapper);
        List<Guide> guides = pageResult.getRecords();

        Set<Long> userIdSet = guides.stream().map(Guide::getUserId).collect(Collectors.toSet());
        Set<Integer> gameIdSet = guides.stream().map(Guide::getGameId).collect(Collectors.toSet());

        Map<Long, User> userMap = new HashMap<>();
        if (!userIdSet.isEmpty()) userMapper.selectBatchIds(userIdSet.stream().toList()).forEach(u -> userMap.put(u.getId(), u));

        Map<Integer, GameCategory> gameMap = new HashMap<>();
        if (!gameIdSet.isEmpty()) gameCategoryMapper.selectBatchIds(gameIdSet.stream().toList()).forEach(g -> gameMap.put(g.getId(), g));

        Set<Long> favSet = Collections.emptySet();
        if (userId != null && !guides.isEmpty()) {
            List<Long> guideIds = guides.stream().map(Guide::getId).toList();
            favSet = guideFavoriteMapper.selectList(new LambdaQueryWrapper<GuideFavorite>()
                    .eq(GuideFavorite::getUserId, userId).in(GuideFavorite::getGuideId, guideIds))
                    .stream().map(GuideFavorite::getGuideId).collect(Collectors.toSet());
        }

        final Set<Long> finalFav = favSet;
        List<GuideListResp.Item> items = guides.stream().map(g -> {
            User u = userMap.get(g.getUserId());
            GameCategory gc = gameMap.get(g.getGameId());
            return GuideListResp.Item.builder()
                    .id(g.getId()).title(g.getTitle()).coverUrl(g.getCoverUrl()).summary(g.getSummary())
                    .authorNickname(u != null ? u.getNickname() : "匿名")
                    .gameName(gc != null ? gc.getName() : "")
                    .viewCount(g.getViewCount()).likeCount(g.getLikeCount()).sectionCount(g.getSectionCount())
                    .isFavorited(finalFav.contains(g.getId()))
                    .createdAt(g.getCreatedAt() != null ? g.getCreatedAt().format(FMT) : "").build();
        }).collect(Collectors.toList());

        return GuideListResp.builder().items(items).total(pageResult.getTotal()).page(page)
                .hasMore(pageResult.getCurrent() < pageResult.getPages()).build();
    }

    @Override
    public GuideDetailResp detail(Long id, Long currentUserId) {
        Guide guide = guideMapper.selectById(id);
        if (guide == null || guide.getStatus() != 1) throw new BusinessException("攻略不存在");

        guideMapper.incrementViewCount(id);
        guide.setViewCount(guide.getViewCount() + 1);

        User author = userMapper.selectById(guide.getUserId());
        GameCategory gc = gameCategoryMapper.selectById(guide.getGameId());

        List<GuideSection> sections = sectionMapper.selectList(
                new LambdaQueryWrapper<GuideSection>().eq(GuideSection::getGuideId, id).orderByAsc(GuideSection::getSortOrder));

        boolean faved = false;
        if (currentUserId != null) {
            faved = guideFavoriteMapper.exists(new LambdaQueryWrapper<GuideFavorite>()
                    .eq(GuideFavorite::getUserId, currentUserId).eq(GuideFavorite::getGuideId, id));
        }

        return GuideDetailResp.builder()
                .id(guide.getId()).title(guide.getTitle()).coverUrl(guide.getCoverUrl()).summary(guide.getSummary())
                .author(GuideDetailResp.UserBrief.builder().id(author != null ? author.getId() : 0L)
                        .nickname(author != null ? author.getNickname() : "匿名").avatar(author != null ? author.getAvatar() : "").build())
                .gameName(gc != null ? gc.getName() : "")
                .viewCount(guide.getViewCount()).likeCount(guide.getLikeCount()).favoriteCount(guide.getFavoriteCount())
                .isFavorited(faved)
                .sections(sections.stream().map(s -> GuideDetailResp.Section.builder()
                        .id(s.getId()).sortOrder(s.getSortOrder()).subtitle(s.getSubtitle())
                        .content(s.getContent()).imageUrl(s.getImageUrl()).build()).collect(Collectors.toList()))
                .createdAt(guide.getCreatedAt() != null ? guide.getCreatedAt().format(FMT) : "").build();
    }

    @Override
    @Transactional
    public GuideDetailResp create(Long userId, GuideCreateReq req) {
        Guide guide = new Guide();
        guide.setUserId(userId);
        guide.setGameId(req.getGameId() != null ? req.getGameId() : 0);
        guide.setTitle(req.getTitle());
        guide.setCoverUrl(req.getCoverUrl() != null ? req.getCoverUrl() : "");
        guide.setSummary(req.getSummary() != null ? req.getSummary() : "");
        guide.setSectionCount(req.getSections() != null ? req.getSections().size() : 0);
        guide.setStatus(1);
        guideMapper.insert(guide);

        if (req.getSections() != null) {
            for (int i = 0; i < req.getSections().size(); i++) {
                GuideCreateReq.Section s = req.getSections().get(i);
                GuideSection gs = new GuideSection();
                gs.setGuideId(guide.getId());
                gs.setSortOrder(i);
                gs.setSubtitle(s.getSubtitle() != null ? s.getSubtitle() : "");
                gs.setContent(s.getContent() != null ? s.getContent() : "");
                gs.setImageUrl(s.getImageUrl() != null ? s.getImageUrl() : "");
                sectionMapper.insert(gs);
            }
        }

        return detail(guide.getId(), userId);
    }

    @Override
    public void delete(Long userId, Long guideId) {
        Guide guide = guideMapper.selectById(guideId);
        if (guide == null || !guide.getUserId().equals(userId)) throw new BusinessException("无权删除");
        guide.setStatus(3);
        guideMapper.updateById(guide);
    }

    @Override
    @Transactional
    public void toggleFavorite(Long userId, Long guideId) {
        Guide guide = guideMapper.selectById(guideId);
        if (guide == null) throw new BusinessException("攻略不存在");
        GuideFavorite exist = guideFavoriteMapper.selectOne(new LambdaQueryWrapper<GuideFavorite>()
                .eq(GuideFavorite::getUserId, userId).eq(GuideFavorite::getGuideId, guideId));
        if (exist != null) {
            guideFavoriteMapper.deleteById(exist.getId());
            guideMapper.decrementFavoriteCount(guideId);
        } else {
            GuideFavorite gf = new GuideFavorite(); gf.setUserId(userId); gf.setGuideId(guideId);
            guideFavoriteMapper.insert(gf);
            guideMapper.incrementFavoriteCount(guideId);
        }
    }
}
