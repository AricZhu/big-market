package com.platform.bigmarket.domain.strategy.service.chain;

import com.platform.bigmarket.domain.strategy.model.entity.RaffleAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;

public interface IRuleFilterChain extends IRuleFilterChainAssemble {
    RaffleAwardEntity filter(RuleFilterEntity ruleFilterEntity);
}
