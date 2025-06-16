package com.huaifang.yan.collection;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class User {
    private String name;
    private int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime;
    }

    public static void main(String[] args) {
        LinkedHashMap<User, String> map = new LinkedHashMap<>();
        map.put(new User("A",1), "<UNK>");
        map.put(new User("B",2), "<UNK>");

        map.get(new User("B",3));
        map.put(new User("C",3), "<UNK>");

        Map<User, String> map2 = new LinkedHashMap<>();
/*        // absent 不存在 是存放
        map2.putIfAbsent(new User("D",4), k -> "<UNK>");
        //不存在是 计算并存放
        map2.computeIfAbsent(new User("D",5), k -> "<UNK>");
        map2.computeIfPresent(new User())*/

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 12, 3, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("测时线程");
                return thread;
            }
        },new ThreadPoolExecutor.AbortPolicy());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("<UNK>");
            }

        });
    }
}
