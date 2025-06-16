package com.huaifang.yan;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class SpringBootProviderMain {
    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(SpringBootProviderMain.class, args);
    }
}
