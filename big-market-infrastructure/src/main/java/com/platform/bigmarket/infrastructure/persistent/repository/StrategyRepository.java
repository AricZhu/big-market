package com.platform.bigmarket.infrastructure.persistent.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyAwardDao;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyDao;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyRuleDao;
import com.platform.bigmarket.infrastructure.persistent.po.Strategy;
import com.platform.bigmarket.infrastructure.persistent.po.StrategyAward;
import com.platform.bigmarket.infrastructure.persistent.po.StrategyRule;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {
    private static final Map<String, Object> CacheData = new HashMap<>();

    @Autowired
    private IStrategyAwardDao strategyAwardDao;

    @Autowired
    private IStrategyRuleDao strategyRuleDao;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IStrategyDao strategyDao;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId) {
        List<StrategyAward> strategyAwardList = strategyAwardDao.queryStrategyAwardListById(strategyId);

        ArrayList<StrategyAwardEntity> strategyAwardEntityList = new ArrayList<>();

        for (StrategyAward strategyAward : strategyAwardList) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntityList.add(strategyAwardEntity);
        }

        return strategyAwardEntityList;
    }

    @Override
    public StrategyEntity queryStrategyEntity(Long strategyId) {
        Strategy strategy = strategyDao.queryStrategy(strategyId);
        if (null == strategy) {
            throw new BizException(ExceptionCode.ILLEGAL_PARAMS.getCode(), "策略 id=" + strategyId + " 查询为空");
        }
        StrategyEntity strategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, strategyEntity);

        return strategyEntity;
    }

    @Override
    public Map<String, Integer> getStrategyAwardRateTable(String key) {
        if (CacheData.containsKey(key)) {
            return (Map<String, Integer>) CacheData.get(key);
        }

        Map<String, Integer> value = (Map<String, Integer>) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStrategyAwardRateTable(String key, Map<String, Integer> rateTable) {
        redisService.setValue(key, rateTable);
    }

    @Override
    public Integer getStragetyAwardRange(String key) {
        if (CacheData.containsKey(key)) {
            return (Integer) CacheData.get(key);
        }

        Integer value = (Integer) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStragetyAwardRange(String key, Integer range) {
        redisService.setValue(key, range);
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel, Integer awardId) {
        StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyId, ruleModel, awardId);
        if (null == strategyRule) {
            return null;
        }
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        BeanUtils.copyProperties(strategyRule, strategyRuleEntity);

        return strategyRuleEntity;
    }
}
