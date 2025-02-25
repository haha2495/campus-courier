package com.example.campus.club.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.club.model.entity.Courier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourierMapper extends BaseMapper<Courier> {
    
    @Select("SELECT * FROM courier WHERE user_id = #{userId}")
    Courier selectByUserId(Long userId);
} 