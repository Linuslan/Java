package com.craftsman.service.impl;

import com.craftsman.mapper.GamePlayerMapper;
import com.craftsman.model.GamePlayer;
import com.craftsman.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ITestServiceImpl implements ITestService {
    @Autowired
    GamePlayerMapper gamePlayerMapper;
    @Override
    public String doTest(Map<String, Object> map) {
        System.out.println("调用失败");
        return "failure";
    }
}
