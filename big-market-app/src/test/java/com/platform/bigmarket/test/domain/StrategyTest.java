package com.platform.bigmarket.test.domain;

import com.platform.bigmarket.domain.strategy.service.strategy.IStrategyAssemble;
import com.platform.bigmarket.domain.strategy.service.strategy.IStrategyLottery;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {
    private static final Long strategyId = 100006L;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IStrategyAssemble strategyAssemble;

    @Autowired
    private IStrategyLottery strategyLottery;


    @Test
    public void test_assembleStrategy() {
        boolean result = strategyAssemble.assembleStrategy(strategyId);
        log.info("策略装配: {}", result);
    }

    @Test
    public void test_doLottery() {
        Integer awardId = strategyLottery.doLottery(strategyId);
        log.info("进行抽奖: {}", awardId);
    }

    @Test
    public void test_doLotteryWeight() {
        Integer awardId = strategyLottery.doLotteryByWeight(strategyId, 4000);
        log.info("进行权重抽奖: {}", awardId);
    }

    /**
     * 模拟抽奖 100 万次，看下各个奖品的概率
     */
    @Test
    public void test_awardRate() {
        int count = 1000000;
        Map<Integer, Integer> awardIdCountMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Integer awardId = strategyLottery.doLottery(strategyId);
            Integer idCount = awardIdCountMap.get(awardId);
            if (idCount == null) {
                idCount = 1;
            } else {
                idCount++;
            }
            awardIdCountMap.put(awardId, idCount);
        }

        awardIdCountMap.forEach((key, value) -> {
            log.info("奖品: {}, 中奖概率为: {}", key, (double)value / count);
        });
    }

    @Test
    public void test_redisMap() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);
        map.put(2, 20);
    }

    /**
     * 测试并发下的库存扣减
     */
    @Test
    public void test_stockDecr() throws InterruptedException {
        redisService.setAtomicLong("stock", 100);
        // 保存结果
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());

        CountDownLatch countDownLatch = new CountDownLatch(2000);

        for (int i = 0; i < 2000; i++) {
            new Thread(() -> {
                // 通过定时来大概对齐所有现成，模拟并发行为
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 使用 redission 提供的原子操作解决并发问题
                long ret = redisService.decr("stock");
                // 扣减成功
                if (ret >= 0) {
                    list.add(1);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        log.info("共有 {} 个线程扣减成功", list.size());
    }
}
