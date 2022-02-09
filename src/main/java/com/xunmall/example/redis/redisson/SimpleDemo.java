package com.xunmall.example.redis.redisson;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xunmall.example.java.util.SnowflakeIdUtils;
import org.redisson.Redisson;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.*;

/**
 * @author wangyj03@zenmen.com
 * @description
 * @date 2021/5/10 20:12
 */
public class SimpleDemo {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("10.100.31.12:6406").setPassword("Ed*C@j5yfW").setDatabase(1);
        RedissonClient redisson = Redisson.create(config);
        String keyName = "udata@activeInfoSet";
        RSetCache<String> set = redisson.getSetCache(keyName);
        int countSize = 500000;
        ExecutorService executorService = new ThreadPoolExecutor(100, 100, 300000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1000),
                new ThreadFactoryBuilder().setNameFormat("redissonThreadPool-").build(), new ThreadPoolExecutor.CallerRunsPolicy());
        CountDownLatch countDownLatch = new CountDownLatch(countSize);
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < countSize; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    set.addAsync(SnowflakeIdUtils.uniqueLong() + "", 60, TimeUnit.MINUTES);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println(System.currentTimeMillis() - startTime);
        System.exit(0);
    }

}
