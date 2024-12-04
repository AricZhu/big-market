package com.platform.bigmarket.test.domain;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleParamsEntity;
import com.platform.bigmarket.domain.strategy.service.IRaffleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RaffleServiceTest {
    @Autowired
    private IRaffleService raffleService;

    @Test
    public void test_performRaffle() {
        RaffleParamsEntity raffleParamsEntity = new RaffleParamsEntity();
        raffleParamsEntity.setUserId("xiaohong");
        raffleParamsEntity.setStrategyId(100001L);

        RaffleAwardEntity raffleAwardEntity = raffleService.performRaffle(raffleParamsEntity);
        log.info("执行抽奖: {}, 结果: {}", JSON.toJSONString(raffleParamsEntity), JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffleBlackList() {
        RaffleParamsEntity raffleParamsEntity = new RaffleParamsEntity();
        raffleParamsEntity.setUserId("user001");
        raffleParamsEntity.setStrategyId(100001L);

        RaffleAwardEntity raffleAwardEntity = raffleService.performRaffle(raffleParamsEntity);
        log.info("执行抽奖: {}, 结果: {}", JSON.toJSONString(raffleParamsEntity), JSON.toJSONString(raffleAwardEntity));
    }
}
