package com.craftsman.mapper;

import com.craftsman.model.GamePlayer;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GamePlayer record);

    int insertSelective(GamePlayer record);

    GamePlayer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GamePlayer record);

    int updateByPrimaryKey(GamePlayer record);
}