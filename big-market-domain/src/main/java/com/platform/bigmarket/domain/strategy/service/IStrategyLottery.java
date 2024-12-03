package com.platform.bigmarket.domain.strategy.service;

public interface IStrategyLottery {
    Integer doLottery(Long strategyId);
    Integer doLotteryByWeight(Long strategyId, Integer weight);
}
