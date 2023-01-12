package com.huaifang.yan.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
/*    @Bean(destroyMethod = "shutdown")
    RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        return Redisson.create(config);
    }*/
}
