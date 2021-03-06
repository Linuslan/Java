package com.craftsman.service;

import com.craftsman.model.GamePlayer;

public interface IGamePlayerService {
    public boolean addPlayer(GamePlayer player);

    public boolean updateSocketId(String socketId, Long playerId) throws Exception;
}
