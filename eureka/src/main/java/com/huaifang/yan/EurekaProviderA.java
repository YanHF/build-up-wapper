package com.huaifang.yan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@ComponentScan(basePackages ="org.example.yanhf.controller.provider",useDefaultFilters = false)
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderA {

    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "providerA");
        SpringApplication.run(EurekaProviderA.class, args);
    }
}
