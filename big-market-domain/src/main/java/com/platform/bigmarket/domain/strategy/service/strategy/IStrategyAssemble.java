package com.platform.bigmarket.domain.strategy.service.strategy;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IStrategyAssemble {
    boolean assembleStrategy(Long strategyId);

    void assembleStrategy(String key, List<StrategyAwardEntity> strategyAwardEntityList);

    Boolean subtractionAwardStock(Long strategyId, Integer awardId);
}
