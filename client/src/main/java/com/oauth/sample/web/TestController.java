package com.oauth.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-04-07 12:34
 */
@Controller
public class TestController {
    @GetMapping(value = "/test")
    public String test(){
        System.out.println("回调地址测试！！");
        return "success";
    }
}
