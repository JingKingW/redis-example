package com.xunmall.example.redis.lock;

/**
 * Created by Gimgoog on 2018/6/11.
 */
public class DistributedLockService {

    DistributedLock lock = new DistributedLock();

    int n = 500;

    public void seckill() {
        // 返回锁的value值，供释放锁时候进行判断
        String indentifier = lock.lockWithTimeout("resource", 5000, 1000);
        System.out.println(Thread.currentThread().getName() + "获得了锁");
        System.out.println(--n);
        lock.releaseLock("resource", indentifier);
    }
}
