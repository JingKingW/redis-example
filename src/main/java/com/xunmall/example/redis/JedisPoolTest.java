package com.xunmall.example.redis;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/2/8 17:43
 */
public class JedisPoolTest {

    private JedisPool pool;

    @Before
    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        config.setTestWhileIdle(false);
        pool = new JedisPool(config, "10.100.31.12", 6404, 3000, "Ed*C@j5yfW", 2);
    }

    @After
    public void destroy() {
        if (pool != null) {
            pool.close();
        }
    }

    @Test
    public void testSetHashWithOutPipeline() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        jedis.flushDB();
        Map<String, String> data = new HashMap<String, String>();
        //直接hmset
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k_" + i, "v_" + i);
            jedis.hmset("key_" + i, data);
        }
        long end = System.currentTimeMillis();
        System.out.println("dbsize:[" + jedis.dbSize() + "] .. ");
        System.out.println("hmset without pipeline used [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testSetHashWithPipeline() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        jedis.flushDB();
        Map<String, String> data = new HashMap<String, String>();
        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k_" + i, "v_" + i);
            pipeline.hmset("key_" + i, data);
        }
        pipeline.sync();
        long end = System.currentTimeMillis();
        System.out.println("dbsize:[" + jedis.dbSize() + "] .. ");
        System.out.println("hmset with pipeline used [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testGetHashWithOutPipeline() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        Set<String> keys = jedis.keys("*");
        //直接使用Jedis hgetall
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        for (String key : keys) {
            result.put(key, jedis.hgetAll(key));
        }
        long end = System.currentTimeMillis();
        System.out.println("result size:[" + result.size() + "] ..");
        System.out.println("hgetAll without pipeline used [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testGetHashWithPipeline() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        Set<String> keys = jedis.keys("*");
        Pipeline pipeline = jedis.pipelined();
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        Map<String, Response<Map<String, String>>> responses = new HashMap<String, Response<Map<String, String>>>(keys.size());
        for (String key : keys) {
            responses.put(key, pipeline.hgetAll(key));
        }
        pipeline.sync();
        for (String k : responses.keySet()) {
            result.put(k, responses.get(k).get());
        }
        long end = System.currentTimeMillis();
        System.out.println("result size:[" + result.size() + "] ..");
        System.out.println("hgetAll with pipeline used [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testSetString() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        jedis.flushDB();
        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            pipeline.set("key_" + i, "value_" + i);
        }
        pipeline.sync();
        long end = System.currentTimeMillis();
        System.out.println("dbsize:[" + jedis.dbSize() + "] .. ");
        System.out.println("testSetString used [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testGetStringWithBatch() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        List<String> keys = Lists.newArrayList();
        keys.add("key_100");
        keys.add("key_101");
        keys.add("key_102");
        keys.add("key_103");
        keys.add("key_104");
        keys.add("key_105");
        keys.add("key_106");
        keys.add("mmc_100");
        keys.add("mmc_101");
        keys.add("mmc_102");
        keys.add("mmc_103");
        List<String> resultString = jedis.mget(keys.stream().toArray(String[]::new));
        resultString.forEach(item->{
            System.out.println(item);
        });
        long end = System.currentTimeMillis();
        System.out.println("result size:[" + resultString.size() + "] ..");
        System.out.println("mget batch [" + (end - start) + "] milliseconds ..");
    }

    @Test
    public void testGetStringWithPipeline() {
        long start = System.currentTimeMillis();
        Jedis jedis = pool.getResource();
        jedis.select(8);
        Set<String> keys = jedis.keys("*");
        Pipeline pipeline = jedis.pipelined();
        Map<String, String> result = new HashMap<String, String>();
        Map<String, Response<String>> responses = new HashMap<String, Response<String>>(keys.size());
        for (String key : keys) {
            responses.put(key, pipeline.get(key));
        }
        pipeline.sync();
        for (String k : responses.keySet()) {
            result.put(k, responses.get(k).get());
        }
        long end = System.currentTimeMillis();
        System.out.println("result size:[" + result.size() + "] ..");
        System.out.println("get with pipeline used [" + (end - start) + "] milliseconds ..");
    }

}
