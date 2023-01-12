package com.huaifang.yan.concurrent;

import java.util.Arrays;

public class VolatilePrint {
    private  static  volatile Long[] arr=new Long[]{0L,0L,0L,0L,0L,0L,0L,0L,0L,0L};
    public static void main(String[] args) throws InterruptedException {
        for (int i=0;i<10;i++){
            int finalI = i;
            Thread thread=new Thread(()->{
                for (int j=0;j<10000;j++){
                    arr[finalI]++;
                }
            });
            thread.start();
        }
        Thread.sleep(3000);
        System.out.println(Arrays.toString(arr));
    }
}
