package com.huaifang.yan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMain
{
    public static void main( String[] args )
    {
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(SpringBootMain.class, args);
    }
}
