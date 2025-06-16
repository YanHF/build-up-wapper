package com.huaifang.yan.concurrent;

import java.util.*;

public class LeetCode {
/*    public static int[]  twoSum(int[] nums, int target) {
        List<Integer> list=new ArrayList();
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                if(nums[i]+nums[j]==target){
                    list.add(i);
                    list.add(j);
                }
            }
        }
        return list.stream().mapToInt(i->i).toArray();
    }*/

    public static int[]  twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]),i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if (map.containsKey(key)) {
                map.get(key).add(str);
            }
            List<String> list = new ArrayList<>();
            list.add(str);
            map.putIfAbsent(key, list);
        }
        return new ArrayList<>(map.values()) ;
    }

    public static void main(String[] args) {
        System.out.println(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}).toString());
    }
}
