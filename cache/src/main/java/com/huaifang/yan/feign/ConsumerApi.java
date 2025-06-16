package com.huaifang.yan.feign;



import org.springframework.web.bind.annotation.GetMapping;


public interface ConsumerApi {

    @GetMapping("/consumer")
    String  getConsumer() ;
}
