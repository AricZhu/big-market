package com.platform.bigmarket.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDemo {
    private int count;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                RLock myLock = this.redissonClient.getLock("myLock");
                myLock.lock();
                this.count++;
                myLock.unlock();
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        log.info("count: {}", count);
    }
}
