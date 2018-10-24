package com.craftsman.controller;

import com.craftsman.model.GamePlayer;
import com.craftsman.service.IGamePlayerService;
import com.craftsman.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private ITestService testService;

    @Autowired
    private IGamePlayerService gamePlayerService;

    @RequestMapping(value="/test")
    public String test(@RequestParam String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        String result = testService.doTest(map);
        return "test "+name+", test result="+result;
    }

    @RequestMapping(value="/addPlayer")
    public String addPlayer(@RequestParam String loginName, @RequestParam String userName
            , @RequestParam String password, @RequestParam String idNumber) {
        GamePlayer player = new GamePlayer();
        player.setCreateTime(new Date());
        player.setIdNumber(idNumber);
        player.setLoginName(loginName);
        player.setLoginTime(new Date());
        player.setPassword(password);
        player.setUpdateTime(new Date());
        player.setUserName(userName);
        if(this.gamePlayerService.addPlayer(player)) {
            return "success";
        } else {
            return "failure";
        }
    }
}
