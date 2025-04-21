package com.huaifang.yan;

import com.huaifang.yan.lock.PrintTest;

/**
 * Hello world!
 *
 */
public class App 
{
/*    public static void main( String[] args ) throws InterruptedException {
        PrintTest printTest = new PrintTest();

       for (int j = 0; j < 500; j++) {
           Thread[] threadsName=new Thread[10];
           for (int i = 0; i < threadsName.length; i++) {
               int finalI = i;
               threadsName[i]= new Thread(()->{
                   printTest.printName("YAN");
               });
               threadsName[i].start();
           }
           Thread[] threadsSix=new Thread[10];
           for (int i = 0; i < threadsSix.length; i++) {
               int finalI = i;
               threadsSix[i]= new Thread(()->{
                   printTest.printSix("男");
               });
               threadsSix[i].start();
           }
       }

       Thread.sleep(5000);
    }*/

    public static void main(String[] args) {
        String str="株式会社誠信スピリット";
        System.out.println(str.toUpperCase());
    }
}
