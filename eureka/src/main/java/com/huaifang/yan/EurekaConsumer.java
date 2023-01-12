package com.huaifang.yan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@ComponentScan(basePackages = "org.example.yanhf.controller.consumer",useDefaultFilters = false)
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumer {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }

    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "consumer");
        SpringApplication.run(EurekaConsumer.class, args);
    }
}
