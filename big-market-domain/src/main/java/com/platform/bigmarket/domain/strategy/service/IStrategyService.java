package com.platform.bigmarket.domain.strategy.service;

public interface IStrategyService {
    /** 初始化抽奖策略 */
    boolean assembleStrategy(Long strategyId);
    /** 开始抽奖 */
    Integer lotteryByStrategyId(Long strategyId);
}
