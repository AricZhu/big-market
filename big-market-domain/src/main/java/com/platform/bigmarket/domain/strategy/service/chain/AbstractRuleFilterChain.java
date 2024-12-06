package com.platform.bigmarket.domain.strategy.service.chain;


public abstract class AbstractRuleFilterChain implements IRuleFilterChain {
    private IRuleFilterChain nextRuleFilterChain;

    @Override
    public IRuleFilterChain next() {
        return nextRuleFilterChain;
    }

    @Override
    public IRuleFilterChain appendNext(IRuleFilterChain ruleFilterChain) {
        this.nextRuleFilterChain = ruleFilterChain;
        return ruleFilterChain;
    }
}
