package com.huaifang.yan.provider;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.huaifang.yan.service.CacheObject;
import com.huaifang.yan.service.LimitingService;
import com.huaifang.yan.service.RedissonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
public class ProviderController {

    private static LoadingCache<String,String> cache;

    @Resource
    private RedissonService redissonService;
    @Resource
    private CacheObject cacheObject;

    @Resource
    private LimitingService limitingService;

    @PostConstruct
    private void init(){
        cache = CacheBuilder
                .newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .weakValues()
                .maximumSize(1)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        try {
                            System.out.println("加载数据");
                            return cacheObject.get(key);
                        }
                        catch (Exception e){
                            return " ";
                        }
                    }
                });
    }

    @RequestMapping("/hello")
    public String index() throws ExecutionException, InterruptedException {
        System.gc();
        Thread.sleep(5000);
        return cache.get("1");
    }

    @RequestMapping("/limit")
    public String limit() {
        for(int i=0;i<10;i++){
            int finalI = i;
            Thread thread=new Thread(()->{
                List<String> keys=new ArrayList<>();
                keys.add(limitingService.key);
                List<String> values=new ArrayList<>();
                String value=Thread.currentThread().getName()+"**"+ finalI;
                values.add(value);
                values.add(String.valueOf(System.currentTimeMillis()));
                values.add(limitingService.limit);
                values.add(String.valueOf(System.currentTimeMillis()-1000*60*30));
                values.add(String.valueOf(System.currentTimeMillis()-1000*20));
              if(limitingService.tryAcquire(keys,values)){
                  System.out.println("获取到令牌");
                  try {
                      Random random=new Random();
                      Integer temp= random.nextInt(5)+8;
                      Thread.sleep(temp);
                      if(temp%3!=0){
                          limitingService.delete(value);
                      }
                  } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                  }finally {
                  }
              }else {
                  System.out.println("未获取到令牌");
              };
            });
            thread.start();
        }
        return "success";
    }
    @RequestMapping("/update")
    public String update(@RequestParam("num") String num)  {
        limitingService.limit=num;
          return "success";
    }

    @RequestMapping("/total")
    public Long total() throws ExecutionException, InterruptedException {
        return limitingService.total();
    }

    @RequestMapping("/lock")
    public String lock() throws ExecutionException, InterruptedException {
        redissonService.lock();
        return "success";
    }
}
