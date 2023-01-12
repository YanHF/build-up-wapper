package com.huaifang.yan.consumer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {




    @RequestMapping("/consumer")
    public String index() {
        return null;
    }
}
