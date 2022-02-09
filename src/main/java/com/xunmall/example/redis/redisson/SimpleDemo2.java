package com.xunmall.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/5/13 16:31
 */
public class SimpleDemo2 {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("10.100.31.12:6406").setPassword("Ed*C@j5yfW").setDatabase(1);
        RedissonClient redisson = Redisson.create(config);
        String keyName = "udata@activeInfoSet";
        RSetCache<String> set = redisson.getSetCache(keyName);
        System.out.println(set.size());
    }

}
