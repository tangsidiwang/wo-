package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.Guide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GuideMapper extends BaseMapper<Guide> {
    @Update("UPDATE guides SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
    @Update("UPDATE guides SET favorite_count = favorite_count + 1 WHERE id = #{id}")
    int incrementFavoriteCount(Long id);
    @Update("UPDATE guides SET favorite_count = GREATEST(0, favorite_count - 1) WHERE id = #{id}")
    int decrementFavoriteCount(Long id);
}
