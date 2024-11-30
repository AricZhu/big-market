package com.platform.bigmarket.test;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.config.RedisProperties;
import com.platform.bigmarket.infrastructure.persistent.dao.IAwardDao;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyAwardDao;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyDao;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyRuleDao;
import com.platform.bigmarket.infrastructure.persistent.po.Award;
import com.platform.bigmarket.infrastructure.persistent.po.Strategy;
import com.platform.bigmarket.infrastructure.persistent.po.StrategyAward;
import com.platform.bigmarket.infrastructure.persistent.po.StrategyRule;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class InfrastructureTest {
    @Autowired
    private IAwardDao awardDao;

    @Autowired
    private IStrategyDao strategyDao;

    @Autowired
    private IStrategyAwardDao strategyAwardDao;

    @Autowired
    private IStrategyRuleDao strategyRuleDao;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private RedisProperties redisProperties;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("query award list: {}", JSON.toJSONString(awards));
    }

    @Test
    public void test_queryStrategyList() {
        List<Strategy> strategies = strategyDao.queryStrategyList();
        log.info("query strategie list: {}", JSON.toJSONString(strategies));
    }

    @Test
    public void test_queryStrategyAwardList() {
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        log.info("query strategyAward list: {}", JSON.toJSONString(strategyAwards));
    }

    @Test
    public void test_queryStrategyRuleList() {
        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
        log.info("query strategyRule list: {}", JSON.toJSONString(strategyRules));

    }

    @Test
    public void test_redisService() {
//        redisService.setValue("name", "hello");

        String name = (String) redisService.getValue("name");
        log.info("get name from redis: {}", name);

        log.info("redis properties: {}", JSON.toJSONString(redisProperties));
    }
}
