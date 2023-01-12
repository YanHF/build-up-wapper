package com.huaifang.yan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerA
{
    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "serverA");
        SpringApplication.run(EurekaServerA.class, args);
    }
}
