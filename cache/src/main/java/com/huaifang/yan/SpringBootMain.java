package com.huaifang.yan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Hello world!
 *
 */
@EnableRetry
@SpringBootApplication
public class SpringBootMain
{
    public static void main( String[] args )
    {

        System.setProperty("spring.profiles.active", "dev");
        ConfigurableApplicationContext context= SpringApplication.run(SpringBootMain.class, args);
        context.registerShutdownHook();
    }
}
