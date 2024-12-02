package com.platform.bigmarket.test.domain;

import com.platform.bigmarket.domain.strategy.service.IStrategyService;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {
    private static final Long strategyId = 100001L;

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    private IRedisService redisService;

    @Test
    public void test_assembleStrategy() {
        boolean result = strategyService.assembleStrategy(strategyId);
        log.info("策略装配: {}", result);
    }

    @Test
    public void test_lotteryByStrategyId() {
        Integer awardId = strategyService.lotteryByStrategyId(strategyId);
        log.info("进行抽奖: {}", awardId);
    }

    /**
     * 模拟抽奖 100 万次，看下各个奖品的概率
     */
    @Test
    public void test_awardRate() {
        int count = 1000000;
        Map<Integer, Integer> awardIdCountMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Integer awardId = strategyService.lotteryByStrategyId(strategyId);
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

        redisService.setHashMap("map2", map);
        Map<Integer, Integer> value = (Map<Integer, Integer>)redisService.getHashMap("map2");
        log.info("get value: {}", value);
        Integer i = value.get(1);
        log.info("get 1: {}", i);
    }
}
