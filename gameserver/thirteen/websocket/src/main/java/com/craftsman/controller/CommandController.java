package com.craftsman.controller;

import com.alibaba.fastjson.JSONObject;
import com.craftsman.common.constant.ClassConstants;
import com.craftsman.common.constant.enums.ErrorCode;
import com.craftsman.common.util.ExceptionUtil;
import com.craftsman.common.util.JSONUtil;
import com.craftsman.service.IGamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommandController {

    @Autowired
    private IGamePlayerService gamePlayerService;

    public Map<String, Object> register(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> login(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> updateSocketId(JSONObject jsonObject) throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        JSONObject dataJson = JSONUtil.getProcessData(jsonObject);
        String socketId = JSONUtil.getSocketId(dataJson);
        Long playerId = JSONUtil.getPlayerId(dataJson);
        this.gamePlayerService.updateSocketId(socketId, playerId);
        return dataMap;
    }

    public Map<String, Object> createRoom(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> enterRoom(JSONObject jsonObject) throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        JSONObject dataJson = JSONUtil.getProcessData(jsonObject);
        Long playerId = JSONUtil.getPlayerId(dataJson);

        return null;
    }

    public Map<String, Object> playerReady(JSONObject jsonObject) {
        return null;
    }

    /**
     * 新游戏倒计时
     * @param jsonObject
     * @return
     */
    public Map<String, Object> newGameCountDown(JSONObject jsonObject) {
        return null;
    }

    /**
     * 新回合倒计时
     * @param jsonObject
     * @return
     */
    public Map<String, Object> newRoundCountDown(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> quitRoom(JSONObject jsonObject) {
        return null;
    }

}
