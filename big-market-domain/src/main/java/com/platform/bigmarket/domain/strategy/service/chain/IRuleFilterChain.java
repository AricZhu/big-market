package com.platform.bigmarket.domain.strategy.service.chain;

import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.service.chain.factory.RuleFilterChainFactory;

public interface IRuleFilterChain extends IRuleFilterChainAssemble {
    RuleFilterChainFactory.RuleFilterChainAwardEntity filter(RuleFilterEntity ruleFilterEntity);
}
