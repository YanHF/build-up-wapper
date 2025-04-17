package com.huaifang.yan.service;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedissonService {

    private Redisson redisson=null;

    //@PostConstruct
    private void init(){
//        String host = "r-2zelm0w7gsexstl98kpd.redis.rds.aliyuncs.com";
//        Config config = new Config();
//        SingleServerConfig singleSerververConfig = config.useSingleServer();
//        singleSerververConfig.setAddress("redis://"+host+":"+6379);
//        singleSerververConfig.setClientName("r-2zelm0w7gsexstl98k");
//        singleSerververConfig.setPassword("Yhf867301!");
//        redisson= (Redisson) Redisson.create(config);
//        RKeys keys= redisson.getKeys();
//        System.out.println(keys.getKeys());
    }

    public void lock(){
        RLock lock= redisson.getLock("yanhuaifanglock");
    /*   if(lock.tryLock()) {
          System.out.println("获取到锁");
       }*/
        lock.unlock();
    }
}
