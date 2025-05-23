package com.huaifang.yan.concurrent;

import java.util.BitSet;
import java.util.concurrent.*;

public class ThreadExchange {

   private static final Executor exchangeExecutor=new ThreadPoolExecutor(0,5,3, TimeUnit.SECONDS, new SynchronousQueue<>(),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());

    private static final Executor priorityExecutor=new ThreadPoolExecutor(1,1,3, TimeUnit.SECONDS, new PriorityBlockingQueue<>(10),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws InterruptedException {

        BitSet bitSet=new BitSet(10);
        bitSet.set(0);
        bitSet.get(2);

        System.out.println(bitSet.cardinality());


/*
        for (int i = 0; i < 100; i++) {
            try {
                priorityExecutor.execute(new ConsumerFunc(i));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        Thread.sleep(1000);
        System.out.println("<UNK>");
*/

/*        Thread thread=new Thread(()->{
            Integer i=0;
            while (i++<10){
                System.out.println("当前线程"+Thread.currentThread().isInterrupted());
            }
        });
        thread.start();

        Thread.sleep(10);
        thread.interrupt();
        Thread.sleep(100);
        System.out.println(thread.isInterrupted());*/
    }
}
