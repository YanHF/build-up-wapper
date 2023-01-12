package com.huaifang.yan.sort;

import java.util.Arrays;

public class QuickSort {

    private static int partition(Integer[] arr, int start, int end) {
        //ReentrantLock
        // 确定枢轴元素
        int pivot = arr[start];
        // 定义两个指针（引用），一个指向数组左端，一个指向数组右端
        int left = start;
        int right = end;
        while (left < right) {
            // 从右往左扫描，寻找比枢轴元素小的，并填入坑中
            while (left < right && arr[right] >= pivot) {
                right--;
            }
            if (left < right) {
                arr[left++] = arr[right];
            }
            // 从左往右扫描，寻找比枢轴元素大的，并填入新坑中
            while (left < right && arr[left] < pivot) {
                left++;
            }
            if (left < right) {
                arr[right--] = arr[left];
            }
        }
        // 扫描完成后,将枢轴元素填入新坑中
        arr[left] = pivot;
        return left;
    }
    public static void sort(Integer[] a, int low, int high) {

        if (low >= high) {
           return;
        }
        int pivot = partition(a, low, high);
        sort(a, low, pivot - 1);
        sort(a, pivot + 1, high);

    }


    public static void main(String[] args) {

        int N = 10;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100);
        System.out.println("before sort");
        System.out.println(Arrays.toString(arr));
        QuickSort.sort(arr,0,N-1);
        System.out.println("after sort");
        System.out.println(Arrays.toString(arr));
    }
}
