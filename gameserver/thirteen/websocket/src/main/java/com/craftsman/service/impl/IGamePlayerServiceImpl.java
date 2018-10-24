package com.craftsman.service.impl;

import com.craftsman.mapper.GamePlayerMapper;
import com.craftsman.model.GamePlayer;
import com.craftsman.service.IGamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component("gamePlayerService")
public class IGamePlayerServiceImpl implements IGamePlayerService {

    @Autowired
    private GamePlayerMapper gamePlayerMapper;

    public boolean addPlayer(GamePlayer player) {
        int i = this.gamePlayerMapper.insert(player);
        if(i > 0) {
            return true;
        } else {
            return false;
        }
    }

}
