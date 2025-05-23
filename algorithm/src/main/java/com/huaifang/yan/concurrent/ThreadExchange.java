package com.huaifang.yan.concurrent;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.*;

public class ThreadExchange {

   private static final Executor exchangeExecutor=new ThreadPoolExecutor(0,5,3, TimeUnit.SECONDS, new SynchronousQueue<>(),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());

    private static final Executor priorityExecutor=new ThreadPoolExecutor(1,1,3, TimeUnit.SECONDS, new PriorityBlockingQueue<>(10),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.AbortPolicy());

   private static final ExecutorService exchangeExecutor=new ThreadPoolExecutor(0,5,3, TimeUnit.SECONDS, new SynchronousQueue<>(),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());
    public static void main(String[] args) throws InterruptedException {

    private static final Executor priorityExecutor=new ThreadPoolExecutor(1,5,3, TimeUnit.SECONDS, new PriorityBlockingQueue<>(3),new ConsumerThreadFactory("JOB"),new ThreadPoolExecutor.CallerRunsPolicy());
        BitSet bitSet=new BitSet(10);
        bitSet.set(0);
        bitSet.get(2);

    public static void main(String[] args) {
        System.out.println(bitSet.cardinality());

        ArrayList<Thread> threads=new ArrayList<>();
        try{
            Future<String> future= exchangeExecutor.submit(()->{
                Thread.sleep(200);
                return "AA";
            });
          String str= future.get(100,TimeUnit.MILLISECONDS);
          System.out.println(str);
        }catch(Exception e){
            e.printStackTrace();
        }

/*
        for (int i = 0; i < 100; i++) {
            try {
                priorityExecutor.execute(new ConsumerFunc(i));
            }catch (Exception e){
                e.printStackTrace();
            }

/*
        }
        Thread.sleep(1000);
        System.out.println("<UNK>");
*/

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            priorityExecutor.execute(new ConsumerFunc(finalI));
        }*/
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
