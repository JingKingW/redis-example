package com.xunmall.example.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Gimgoog on 2018/7/2.
 */
public class JedisBaseTest {

    private static Jedis jedis = new Jedis("192.168.177.129", 6390);

    static {
        jedis.auth("abc123");
    }

    public static void main(String[] args) {
        Long start = Long.valueOf(getMemory());
        initInsert();
        Long end = Long.valueOf(getMemory());
        System.out.println(end - start);
    }

    private static void initInsert() {
        for (int i = 10000; i < 100000; i++) {
            jedis.set("aa" + i, "bb" + i);
        }
    }

    private static String getMemory() {
        String allLineMemory = jedis.info("memory");
        String spileString = allLineMemory.split("\r\n")[1];
        String memory = spileString.substring(spileString.indexOf(':') + 1);
        return memory;
    }


}
