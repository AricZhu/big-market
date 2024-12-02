package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IStrategyAssemble {
    boolean assembleStrategy(Long strategyId);

    void assembleStrategy(String key, List<StrategyAwardEntity> strategyAwardEntityList);
}
