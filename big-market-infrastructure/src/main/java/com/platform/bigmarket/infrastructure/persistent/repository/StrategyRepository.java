package com.platform.bigmarket.infrastructure.persistent.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.infrastructure.persistent.dao.IStrategyAwardDao;
import com.platform.bigmarket.infrastructure.persistent.po.StrategyAward;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {
    private final String rateRangePrefix = "range_";
    private final String rateTablePrefix = "rate_table_";

    private static final Map<String, Object> CacheData = new HashMap<>();

    @Autowired
    private IStrategyAwardDao strategyAwardDao;

    @Autowired
    private IRedisService redisService;

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
    public Map<String, Integer> getStrategyAwardRateTable(Long strategyId) {
        String key = rateTablePrefix + strategyId.toString();

        if (CacheData.containsKey(key)) {
            return (Map<String, Integer>) CacheData.get(key);
        }

        Map<String, Integer> value = (Map<String, Integer>) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStrategyAwardRateTable(Long strategyId, Map<String, Integer> rateTable) {
        redisService.setValue(rateTablePrefix + strategyId.toString(), rateTable);
    }

    @Override
    public Integer getStragetyAwardRange(Long strategyId) {
        String key = rateRangePrefix + strategyId.toString();

        if (CacheData.containsKey(key)) {
            return (Integer) CacheData.get(key);
        }

        Integer value = (Integer) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStragetyAwardRange(Long strategyId, Integer range) {
        redisService.setValue(rateRangePrefix + strategyId.toString(), range);
    }
}
