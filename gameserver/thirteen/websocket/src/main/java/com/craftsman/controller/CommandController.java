package com.craftsman.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class CommandController {

    public Map<String, Object> register(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> login(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> createRoom(JSONObject jsonObject) {
        return null;
    }

    public Map<String, Object> enterRoom(JSONObject jsonObject) {
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
