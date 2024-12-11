package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;

/**
 * 规则树决策引擎接口
 */
public interface IDecisionEngine {
    DefaultTreeLogicFactory.RuleFilterTreeAwardEntity process(String userId, Long strategyId, Integer awardId);
}
