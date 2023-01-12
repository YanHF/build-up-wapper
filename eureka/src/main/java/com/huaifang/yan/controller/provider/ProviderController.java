package com.huaifang.yan.controller.provider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @RequestMapping("/hello")
    public String index() {
        return "Hello World"+System.getProperty("spring.profiles.active");
    }
}
