package com.huaifang.yan.concurrent;

import org.omg.CORBA.SetOverrideType;
import org.openjdk.jol.info.ClassLayout;

public class ConsumerFunc implements Runnable ,Comparable<ConsumerFunc>{
    public ConsumerFunc(int i) {
        this.i = i;
    }

    private int i;

    @Override
    public int compareTo(ConsumerFunc o) {
        return o.i - i;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("当前线程名称"+Thread.currentThread().getName()+"数据值"+String.valueOf(i));
    }

    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        Thread thread = new Thread(()->{
            for (int i = 0; i < 3; i++) {
                synchronized (o) {
                    System.out.println(ClassLayout.parseInstance(o).toPrintable());
                }
            }
        });
        thread.start();

        for (int i = 0; i < 3; i++) {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        }

    }
}
