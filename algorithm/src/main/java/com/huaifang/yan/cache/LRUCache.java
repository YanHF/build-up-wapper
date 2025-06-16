package com.huaifang.yan.cache;

import org.springframework.cache.Cache;

import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * LRU 最近最少使用 强调的是最近使用过，不考虑使用的频率，有时效性，一般认为一段时间没访问的数据，之后也不会使用
 *
 *
 */
public class LRUCache implements Cache {

    // 缓存大小
  private   Integer capacity;

  private   Map<String,String> cacheMap;
  // Node 双向 队列
  private LinkedHashMap<String,String> map;

  private String oldKey;



  public LRUCache(Integer capacity) {
      this.capacity = capacity;
      cacheMap=new HashMap<>(capacity);
      map=new LinkedHashMap<String,String>(capacity,0.75f,true){

          @Override
          protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
              if(size()>capacity){
                  oldKey=eldest.getKey();
              }
              return true;
          }
      };
  }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = cacheMap.get(key.toString());
        if (value == null || !type.isInstance(value)) {
            return null;
        }
        map.get(key.toString());
        return (T) value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        cacheMap.put(key.toString(),value.toString());
        if(oldKey!=null){
            map.remove(oldKey);
            oldKey=null;
        }
        map.put(key.toString(),value.toString());
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }


    public static void main(String[] args) {

        BitSet bitSet=new BitSet(65);
        bitSet.set(3);
        bitSet.get(3);
        System.out.println();
    }
}