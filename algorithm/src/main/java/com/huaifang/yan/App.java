package com.huaifang.yan;

import com.huaifang.yan.concurrent.ConsumerCondition;
import com.huaifang.yan.lock.PrintTest;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * Hello world!
 *
 */
public class App 
{
    private static volatile Integer num = 1;
    static ThreadLocal<String> B = new ThreadLocal<>();
    static ThreadLocal<String> A = new ThreadLocal<>();

    private static final Object LOCK = new Object();
/*
    public static void main(String[] args) throws InterruptedException {
        ConsumerCondition consumerCondition = new ConsumerCondition();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    if (consumerCondition.tryAcquire()) {
                        App.num = App.num + 1;
                        consumerCondition.tryRelease();
                        break;
                    }
                }
            });
            thread.start();
        }
        Thread.sleep(200);
        System.out.println(App.num);
    }*/


 /*   // 线程1 A 线程2 B 线程3 C
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("A");
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("B");
        });
        t2.start();
        Thread t3 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("C");
        });
        t3.start();

        TimeUnit.SECONDS.sleep(3);
    }
*/


 /*   // 线程1 A 线程2 B 线程3 C
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (;;){
                if(num%3==1){
                    System.out.println("A");
                    num=num+1;
                }
            }

        });
        t1.setDaemon(true);
        t1.start();
        Thread t2 = new Thread(() -> {
            for (;;){
                if(num%3==2){
                    System.out.println("B");
                    num=num+1;
                }
            }

        });
        t2.setDaemon(true);
        t2.start();
        Thread t3 = new Thread(() -> {
            for (;;){
                if(num%3==0){
                    System.out.println("C");
                    num=1;
                }
            }
        });
        t3.setDaemon(true);
        t3.start();

        TimeUnit.MILLISECONDS.sleep(10);
    }
*/

    // 线程1 A 线程2 B 线程3 C
/*
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (;;){
                synchronized (LOCK) {
                    if (num % 3 == 1) {
                        System.out.println("A");
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        });
        t1.setDaemon(true);
        t1.start();
        Thread t2 = new Thread(() -> {
            for (;;){
                synchronized (LOCK) {
                    if (num % 3 == 2) {
                        System.out.println("B");
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        });
        t2.setDaemon(true);
        t2.start();
        Thread t3 = new Thread(() -> {
            for (;;){
                synchronized (LOCK) {
                    if (num % 3 == 0) {
                        System.out.println("C");
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        });
        t3.setDaemon(true);
        t3.start();

        for (int i = 0; i < 100; i++) {
            synchronized (LOCK) {
                if(num%3==0){
                    num=(num+1)%3;

                }else {
                    num=num+1;
                }
                LOCK.notifyAll();
            }

            TimeUnit.MILLISECONDS.sleep(50);
        }

        TimeUnit.MILLISECONDS.sleep(10);
    }
*/

/*
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (;;){
                if (num % 3 == 1) {
                    System.out.println("A");
                    LockSupport.park();
                }
            }

        });
        t1.setDaemon(true);
        t1.start();
        Thread t2 = new Thread(() -> {
            for (;;){
                if (num % 3 == 2) {
                    System.out.println("B");
                    LockSupport.park();
                }
            }

        });
        t2.setDaemon(true);
        t2.start();
        Thread t3 = new Thread(() -> {
            for (;;){
                if (num % 3 == 0) {
                    System.out.println("C");
                    LockSupport.park();
                }
            }

        });
        t3.setDaemon(true);
        t3.start();

        LinkedList<Thread> threads = new LinkedList<>();
        threads.add(t1);
        threads.add(t2);
        threads.add(t3);
        for (Thread thread : threads) {
            LockSupport.unpark(thread);
            num=num+1;
            TimeUnit.SECONDS.sleep(5);
        }
    }
*/
/*

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (;;){
                if(Thread.interrupted()){
                    break;
                }
                System.out.println(Thread.currentThread().getName() + "<UNK>");
            }
        });
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
        t1.join();
        System.out.println(num);
    }*/

/*
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + "<UNK>");
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "<UNK>");
        });
        t2.start();

        Thread t3 = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "<UNK>");
        });
        t3.start();
       // TimeUnit.SECONDS.sleep(1);
        countDownLatch.await();
        System.out.println(num);
    }*/


    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3,()->{
            System.out.println("条件具备"+Thread.currentThread().getName() + "<UNK>");

        });

        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "一起走<UNK>");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "一起走<UNK>");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        t2.start();

        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "一起走<UNK>");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        t3.start();
        TimeUnit.SECONDS.sleep(6);

        System.out.println(num);
    }

}
