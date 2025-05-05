package com.huaifang.yan.concurrent;

import java.util.concurrent.*;

public class ThreadExchange {

   private static final Executor exchangeExecutor=new ThreadPoolExecutor(0,5,3, TimeUnit.SECONDS, new SynchronousQueue<>(),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());

    private static final Executor priorityExecutor=new ThreadPoolExecutor(1,5,3, TimeUnit.SECONDS, new PriorityBlockingQueue<>(3),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            priorityExecutor.execute(new ConsumerFunc(finalI));
        }
    }
}
