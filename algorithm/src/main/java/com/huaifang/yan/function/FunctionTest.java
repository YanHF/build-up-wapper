package com.huaifang.yan.function;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {
        Function<String,Integer> function= String::length;
        System.out.println(function.apply("Yan"));

        BiFunction<String,String,Integer> biFunction=((o, o2) -> {
            return o.length()+o2.length();
        });
        System.out.println(biFunction.apply("Yan","Yan"));
    }
}
