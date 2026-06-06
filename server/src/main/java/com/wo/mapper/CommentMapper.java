package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Update("UPDATE comments SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    @Update("UPDATE comments SET like_count = GREATEST(0, like_count - 1) WHERE id = #{id}")
    int decrementLikeCount(Long id);
    @Update("UPDATE comments SET reply_count = reply_count + 1 WHERE id = #{id}")
    int incrementReplyCount(Long id);
}
