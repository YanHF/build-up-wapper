package com.huaifang.yan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Hello world!
 *
 */
@ImportResource("")
@EnableRetry
@SpringBootApplication
public class SpringBootMain
{
    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(SpringBootMain.class, args);
    }
}
