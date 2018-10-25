package com.craftsman.service.impl;

import com.craftsman.common.constant.enums.ErrorCode;
import com.craftsman.common.util.ExceptionUtil;
import com.craftsman.mapper.GamePlayerMapper;
import com.craftsman.model.GamePlayer;
import com.craftsman.service.IGamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Component("gamePlayerService")
public class IGamePlayerServiceImpl implements IGamePlayerService {

    @Resource
    private GamePlayerMapper gamePlayerMapper;

    public boolean addPlayer(GamePlayer player) {
        int i = this.gamePlayerMapper.insert(player);
        if(i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateSocketId(String socketId, Long playerId) throws Exception {
        boolean success = false;
        GamePlayer player = this.gamePlayerMapper.selectByPrimaryKey(playerId);
        if(null == player) {
            ExceptionUtil.throwExceptionJsonByError(ErrorCode.PLAYER_NON_EXISTENT);
        }
        player.setSocketId(socketId);
        this.gamePlayerMapper.updateByPrimaryKeySelective(player);
        success = true;
        return success;
    }

}
