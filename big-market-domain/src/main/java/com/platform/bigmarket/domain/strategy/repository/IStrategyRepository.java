package com.platform.bigmarket.domain.strategy.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.StockUpdateTaskDTO;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    StrategyEntity queryStrategyEntity(Long strategyId);

    Map<String, Integer> getStrategyAwardRateTable(String key);

    void setStrategyAwardRateTable(String key, Map<String, Integer> rateTable);

    Integer getStragetyAwardRange(String key);

    void setStragetyAwardRange(String key, Integer range);

    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel, Integer awardId);

    RuleTreeDTO getRuleTree(String treeId);

    void cacheStrategyAward(String key, Integer value);

    Boolean subtractionAwardStock(String key);

    Long getAwardStock(String key);

    void addStockUpdateTask(String key, StockUpdateTaskDTO stockUpdateTaskDTO);

    StockUpdateTaskDTO getStockUpdateTask(String key);

    void updateAwardStock(StockUpdateTaskDTO stockUpdateTaskDTO);

    List<StrategyAwardEntity> queryAwardList(Long strategyId);
}
