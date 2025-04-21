package com.huaifang.yan.lock;

public class SortSet {
    private static final String str =
            "local key = KEYS[1]\n"  //set key
                    + "local value = ARGV[1]\n"// 值
                    + "local score = tonumber(ARGV[2])\n" // 权重
                    + "local maxLen = tonumber(ARGV[3])\n" // 最大允许的set 值多少
                    + "local f = tonumber(ARGV[4])\n" // 扫过期范围
                    + "local t = tonumber(ARGV[5])\n" // 扫过期范围
                    + "if redis.call('EXISTS',key)==1 \n" // 如果key存在
                    + " then\n"
                    + " redis.call('ZREMRANGEBYSCORE', key , f , t)\n"//删除死锁的，没有及时释放的资源
                    + " local result = redis.call('ZCARD', key)\n" //查询当前数量有没有达到最大值
                    + " if(result >= maxLen) \n" //最大获取锁失败
                    + " then \n"
                    + " return 0\n"
                    + " else \n" //set 插入数据
                    + " redis.call('ZADD', key , score , value)\n"
                    + " return 1\n"
                    + " end\n"
                    + "else\n" //直接成功
                    + " redis.call('ZADD', key , score , value)\n"
                    + " return 1\n" + "end";


}
