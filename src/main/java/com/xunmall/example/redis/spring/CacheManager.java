package com.xunmall.example.redis.spring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangyanjing on 2018/11/9.
 */
public class CacheManager<T> {

    private Map<String, T> cache = new ConcurrentHashMap<String, T>();

    public T getValue(Object key) {
        return cache.get(key);
    }

    public void addOrUpdateCache(String key, T value) {
        cache.put(key, value);
    }

    public void evictCache(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }

    public void evictCache() {
        cache.clear();
    }
}
