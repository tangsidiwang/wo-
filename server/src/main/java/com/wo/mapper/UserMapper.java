package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("UPDATE users SET post_count = post_count + 1 WHERE id = #{id}")
    int incrementPostCount(Long id);
    @Update("UPDATE users SET fans_count = fans_count + 1 WHERE id = #{id}")
    int incrementFansCount(Long id);
    @Update("UPDATE users SET fans_count = GREATEST(0, fans_count - 1) WHERE id = #{id}")
    int decrementFansCount(Long id);
    @Update("UPDATE users SET follow_count = follow_count + 1 WHERE id = #{id}")
    int incrementFollowCount(Long id);
    @Update("UPDATE users SET follow_count = GREATEST(0, follow_count - 1) WHERE id = #{id}")
    int decrementFollowCount(Long id);
}
