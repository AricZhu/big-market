package com.platform.bigmarket.domain.strategy.service.tree;

/**
 * 规则树决策引擎接口
 */
public interface IDecisionEngine {
    DefaultTreeLogicFactory.AwardDataEntity process(String userId, Long strategyId, Integer awardId);
}
