package com.craftsman.mapper;

import com.craftsman.model.GameRoom;

public interface GameRoomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameRoom record);

    int insertSelective(GameRoom record);

    GameRoom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameRoom record);

    int updateByPrimaryKey(GameRoom record);
}