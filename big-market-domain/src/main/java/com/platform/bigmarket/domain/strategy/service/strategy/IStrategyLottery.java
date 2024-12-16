package com.platform.bigmarket.domain.strategy.service.strategy;

public interface IStrategyLottery {
    Integer doLottery(Long strategyId);
    Integer doLotteryByWeight(Long strategyId, Integer weight);
}
