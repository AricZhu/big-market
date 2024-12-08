package com.platform.bigmarket.domain.strategy.service.tree;

public interface ITreeLogic {
    DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId);
}
