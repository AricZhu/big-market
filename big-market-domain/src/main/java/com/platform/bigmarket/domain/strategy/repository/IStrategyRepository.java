package com.platform.bigmarket.domain.strategy.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId);

    Map<String, Integer> getStrategyAwardRateTable(Long strategyId);

    void setStrategyAwardRateTable(Long strategyId, Map<String, Integer> rateTable);

    Integer getStragetyAwardRange(Long strategyId);

    void setStragetyAwardRange(Long strategyId, Integer range);
}
