package com.huaifang.yan.concurrent;

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
}
