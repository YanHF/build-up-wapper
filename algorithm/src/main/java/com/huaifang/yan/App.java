package com.huaifang.yan;

import com.huaifang.yan.concurrent.ConsumerCondition;
import com.huaifang.yan.lock.PrintTest;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Integer num = 0;
    static ThreadLocal<String> B = new ThreadLocal<>();
    static ThreadLocal<String> A = new ThreadLocal<>();
    

    public static void main(String[] args) throws InterruptedException {
        ConsumerCondition consumerCondition = new ConsumerCondition();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    if(consumerCondition.tryAcquire()) {
                        App.num = App.num+1;
                        consumerCondition.tryRelease();
                        break;
                    }
                }
            });
            thread.start();
        }
        Thread.sleep(200);
        System.out.println(App.num);
    }
}
