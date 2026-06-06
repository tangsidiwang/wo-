package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.GameCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GameCategoryMapper extends BaseMapper<GameCategory> {
    @Update("UPDATE game_categories SET post_count = post_count + 1 WHERE id = #{id}")
    int incrementPostCount(Integer id);
}
