package com.xunmall.example.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/2/10 14:28
 */
public class JedisSentinelPoolTest {

    private JedisSentinelPool pool;

    @Before
    public void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        config.setTestWhileIdle(false);
        Set<String> sentinelSet = new HashSet<>();
        sentinelSet.add("10.100.31.12:24400");
        pool = new JedisSentinelPool("master_6406", sentinelSet, config, 3000, "Ed*C@j5yfW", 2);
    }


    @Test
    public void testSentinel(){
        Jedis jedis = pool.getResource();
        jedis.set("hello", "world");
        System.out.println(jedis.get("hello"));
    }

    @Test
    public void testBatch(){
        Jedis jedis = pool.getResource();
        System.out.println(jedis.get("1"));
    }
}
