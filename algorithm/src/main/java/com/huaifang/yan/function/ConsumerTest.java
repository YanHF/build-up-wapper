package com.huaifang.yan.function;

import java.util.function.BiConsumer;

public class ConsumerTest {
    public static void main(String[] args) {
        BiConsumer<String,String> biFunction = ConsumerTest::println;
        biFunction.accept("Yan","Yan");

    }

    public static void println(String mng,String str) {
        System.out.println(mng+":"+str);
    }
}
