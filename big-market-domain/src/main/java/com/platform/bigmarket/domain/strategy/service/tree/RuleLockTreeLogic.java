package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import org.springframework.stereotype.Component;

@Component("rule_lock")
public class RuleLockTreeLogic implements ITreeLogic {
    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.ALLOW)
                .build();
    }
}
