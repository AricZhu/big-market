package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;

public interface ITreeLogic {
    DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId, String ruleValue);
}
