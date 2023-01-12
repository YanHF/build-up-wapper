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
public class EurekaServerB
{
    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "serverB");
        SpringApplication.run(EurekaServerB.class, args);
    }
}
