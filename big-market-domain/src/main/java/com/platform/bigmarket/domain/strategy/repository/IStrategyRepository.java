package com.platform.bigmarket.domain.strategy.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId);

    Map<String, Integer> getStrategyAwardRateTable(String key);

    void setStrategyAwardRateTable(String key, Map<String, Integer> rateTable);

    Integer getStragetyAwardRange(String key);

    void setStragetyAwardRange(String key, Integer range);

    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel);
}
