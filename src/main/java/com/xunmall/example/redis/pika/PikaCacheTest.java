package com.xunmall.example.redis.pika;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.util.MurmurHash;

import java.util.Set;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/5/27 16:52
 */
public class PikaCacheTest {

    public static void main(String[] args) {
        System.out.println(getKeyIndex("5495642188317696"));

        Jedis jedis = new Jedis("10.100.31.12", 9222);
        jedis.auth("123456");
        Long zrank = jedis.zrank("udata@online@uid@260", "5331145326412800");
        if (zrank == null) {
            System.out.println(Boolean.FALSE);
        } else {
            System.out.println(Boolean.TRUE);
        }

        Set<Tuple> tupleSet = jedis.zrangeByScoreWithScores("udata@online@uid@220", 0, -1);
        if (tupleSet.size() > 0) {
            tupleSet.forEach(itme -> {
                System.out.println("key:" + itme.getElement() + "value: " + itme.getScore());
            });
        }

        Long zscore = jedis.zscore("udata@online@uid@220", "5450073226560512").longValue();
        System.out.println(zscore);

    }

    private static int getKeyIndex(String id) {
         MurmurHash MURMUR_HASH = new MurmurHash();
        long hash = Math.abs(MURMUR_HASH.hash(id));
        return (int) (hash % 1000);
    }
}
