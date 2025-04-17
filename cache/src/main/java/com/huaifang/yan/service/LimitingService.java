package com.huaifang.yan.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class LimitingService {
    private Set<String> set=new TreeSet<>();

    public String key="yanhuaifang";

    public String limit="10";
    private String str="local key = KEYS[1]\n" +
            "local value = ARGV[1]\n" +
            "local score = tonumber(ARGV[2])\n" +
            "local maxLen = tonumber(ARGV[3])\n" +
            "local f = tonumber(ARGV[4])\n" +
            "local t = tonumber(ARGV[5])\n" +
            "if redis.call('EXISTS',key)==1 \n" +
            " then\n" +
            "   redis.call('ZREMRANGEBYSCORE', key , f , t)\n" +
            "   local result = redis.call('ZCARD', key)\n" +
            "   if(result >= maxLen) \n" +
            "     then \n" +
            "       return 0\n" +
            "    else \n" +
            "      redis.call('ZADD', key , score , value)\n" +
            "      return 1\n" +
            "     end\n" +
            "else\n" +
            "  redis.call('ZADD', key , score , value)\n" +
            "  return 1\n" +
            "end";
    private JedisPool pool;

    //@PostConstruct
    private void init(){

        String host = "r-2zelm0w7gsexstl98kpd.redis.rds.aliyuncs.com";
        JedisPoolConfig config =new JedisPoolConfig();
        config.setMaxTotal(100);//最大提供的连接数
        config.setMaxIdle(10);//最大空闲连接数(即初始化提供了100有效的连接数)
        config.setMinIdle(10);//最小保证的提供的（空闲）连接数
        //创建Jedis连接池
         pool = new JedisPool(config,host,6379);
        //从连接池中得到可用的jedis对象
        Jedis jedis = pool.getResource();
        jedis.auth("r-2zelm0w7gsexstl98k","Yhf867301!");
        jedis.zadd(this.key,System.currentTimeMillis()+6000,"1");
        jedis.close();
    }

    public  boolean tryAcquire(List<String> keys,List<String> values){
        Jedis jedis = this.getJedis();
        Object result= jedis.eval(str,keys,values);
        Boolean b=result.toString().equals("1")? true:false;
        jedis.close();
       return b;
    }

    public Long total(){
        Jedis jedis = this.getJedis();
        Long total= jedis.zcard(this.key);
        jedis.close();
       return total;
    }

    public Long delete(String value){
        Jedis jedis = this.getJedis();
        Long delete= jedis.zrem(this.key,value);
        jedis.close();
        return delete;
    }

    public Jedis getJedis(){
        Jedis jedis = pool.getResource();

        jedis.auth("r-2zelm0w7gsexstl98k","Yhf867301!");
        return jedis;
    }
    }

