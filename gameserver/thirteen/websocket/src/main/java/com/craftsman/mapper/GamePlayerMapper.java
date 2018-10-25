package com.craftsman.mapper;

import com.craftsman.model.GamePlayer;

public interface GamePlayerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GamePlayer record);

    int insertSelective(GamePlayer record);

    GamePlayer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GamePlayer record);

    int updateByPrimaryKey(GamePlayer record);
}