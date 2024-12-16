package com.platform.bigmarket.domain.strategy.service.raffle;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IRaffleAwardService {
    List<StrategyAwardEntity> queryAwardList(Long strategyId);
}
