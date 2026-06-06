package com.wo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wo.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    @Update("UPDATE news SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
}
