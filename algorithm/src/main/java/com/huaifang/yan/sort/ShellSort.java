package com.huaifang.yan.sort;

import java.util.Arrays;

/**
 * 希尔排序，插入排序变种
 */
public class ShellSort {
    //核心代码---开始
    public static void sort(Comparable[] arr) {
        int j;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                Comparable tmp = arr[i];
                for (j = i; j >= gap && tmp.compareTo(arr[j - gap]) < 0; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }
        }
    }
    //核心代码---结束

    //核心代码---开始
    public static void sortA(Integer[] arr) {
        int step;
        for (step = arr.length / 2; step > 0; step /= 2) {
            for (int i = step; i < arr.length; i++) {
                int j = i - step;
                int temp = arr[i];
                for (;j >= 0 && temp < arr[j]; ) {
                    arr[j + step] = arr[j];
                    j -= step;
                }
                arr[j + step] = temp;
            }
        }
    }

    public static void main(String[] args) {

        int N = 10;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100);
        System.out.println("before sort");
        System.out.println(Arrays.toString(arr));
        ShellSort.sortA(arr);
        System.out.println("after sort");
        System.out.println(Arrays.toString(arr));
    }
}
