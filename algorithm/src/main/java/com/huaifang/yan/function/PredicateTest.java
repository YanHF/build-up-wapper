package com.huaifang.yan.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PredicateTest {

    private static final List<Predicate<String>> must=new ArrayList<>();
    private static final List<Predicate<String>> should=new ArrayList<>();
    private static final List<Predicate<String>> mustNot=new ArrayList<>();
    public static void main(String[] args) {

        must.add(PredicateTest::printlnTrue);
        must.add(PredicateTest::printlnFalse);

        Predicate<String> predicate= must.stream().findFirst().get();
        for (Predicate<String> p : must) {
            predicate= predicate.and(p);
        }
        predicate=predicate.negate();
        boolean result= predicate.test("Yan");

        System.out.println(result);

        Predicate<String> predicateTrue=PredicateTest::printlnTrue;
        Predicate<String> printlnFalse=PredicateTest::printlnFalse;

        //Stream.of(1,2,3).collect(Collectors.toMap((a,b)->{a.doubleValue(),}));
    }

    public static boolean printlnTrue(String msg){
        System.out.println("A");
        return true;
    }
    public static boolean printlnFalse(String msg){
        System.out.println("B");
        return false;
    }
    public static boolean println(String... msg){

        return false;
    }
}
