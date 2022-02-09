package com.xunmall.example.redis;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by yanjing_wang on 2017/5/5.
 */
public class SimpleBaseTest {

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.8.34", 6379);
        jedis.auth("move@123");
        jedis.select(1);
        System.out.println("Connection to server successfully");
        //查看服务是否运行
        System.out.println("Server is running: " + jedis.ping());
        //存储数据到列表中
        jedis.lpush("tutorial-list", "Redis");
        jedis.lpush("tutorial-list", "Mongodb");
        jedis.lpush("tutorial-list", "Mysql");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("tutorial-list", 0, 5);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Stored string in redis:: " + list.get(i));
        }
        //jedis.flushDB();
    }
}
