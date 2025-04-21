package com.huaifang.yan.lock;

public class PrintTest {

    public void printName(String str) {
        synchronized (this) {
            System.out.println(str);
        }
        return;
    }

    public void printSix(String str) {
        synchronized (this) {
            System.out.println(str);
        }
        return;
    }
}
