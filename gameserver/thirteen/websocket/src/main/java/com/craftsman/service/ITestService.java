package com.craftsman.service;

import com.craftsman.model.GamePlayer;
import com.craftsman.service.impl.ITestServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "thirteen-basic-service", fallback = ITestServiceImpl.class)
public interface ITestService {
    @RequestMapping(value = "/test")
    public String doTest(@RequestParam Map<String, Object> map);
}
