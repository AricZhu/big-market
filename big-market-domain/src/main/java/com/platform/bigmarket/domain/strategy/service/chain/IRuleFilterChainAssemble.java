package com.platform.bigmarket.domain.strategy.service.chain;

public interface IRuleFilterChainAssemble {
    IRuleFilterChain next();
    IRuleFilterChain appendNext(IRuleFilterChain ruleFilterChain);
}
