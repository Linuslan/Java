package com.craftsman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 必须得是RestController，否则服务调用者会报No Message available
 */
@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public String test(@RequestParam Map<String, Object> map) {
        String name = (String) map.get("name");
        System.out.println("name="+name);
        return "success";
    }
}
