package com.xunmall.example.redis.lock;

/**
 * Created by Gimgoog on 2018/6/11.
 */
class ThreadA extends Thread {
    private DistributedLockService service;

    public ThreadA(DistributedLockService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}

public class LockTestDemo {
    public static void main(String[] args) {
        DistributedLockService service = new DistributedLockService();
        for (int i = 0; i < 50; i++) {
            ThreadA threadA = new ThreadA(service);
            threadA.start();
        }
    }
}