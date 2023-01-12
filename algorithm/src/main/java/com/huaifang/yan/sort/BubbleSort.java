package com.huaifang.yan.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 */
public class BubbleSort {

    public static void sort(Comparable[] arr){
        for (int i = 0; i < arr.length-1; ++i) {
            for (int j = 0; j < arr.length-1 - i; ++j) {
                if (arr[j].compareTo(arr[j + 1])>0)
                {
                    Comparable tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }

    }

    public static void main(String[] args) {

        int N = 10;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100);
        System.out.println("before sort");
        System.out.println(Arrays.toString(arr));
        BubbleSort.sort(arr);
        System.out.println("after sort");
        System.out.println(Arrays.toString(arr));
    }
}
