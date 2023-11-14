package com.huaifang.yan.sort;

import java.util.Arrays;

/**
 * 插入排序
 */
public class InsertionSort {

    public static void sort(Comparable[] arr){

        int n = arr.length;
        for (int i = 0; i < n; i++) {
            // 寻找元素arr[i]合适的插入位置
           for( int j = i ; j > 0 ; j -- )
                if( arr[j].compareTo( arr[j-1] ) < 0 )
                    swap( arr, j , j-1 );
                else
                    break;
        }
    }
    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {

        int N = 10;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100);
        System.out.println("before sort");
        System.out.println(Arrays.toString(arr));
        InsertionSort.sort(arr);
        System.out.println("after sort");
        System.out.println(Arrays.toString(arr));
    }

}