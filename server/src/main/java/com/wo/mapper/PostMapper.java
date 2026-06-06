package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Update("UPDATE posts SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
    @Update("UPDATE posts SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    @Update("UPDATE posts SET like_count = GREATEST(0, like_count - 1) WHERE id = #{id}")
    int decrementLikeCount(Long id);
    @Update("UPDATE posts SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(Long id);
    @Update("UPDATE posts SET favorite_count = favorite_count + 1 WHERE id = #{id}")
    int incrementFavoriteCount(Long id);
    @Update("UPDATE posts SET favorite_count = GREATEST(0, favorite_count - 1) WHERE id = #{id}")
    int decrementFavoriteCount(Long id);
}
