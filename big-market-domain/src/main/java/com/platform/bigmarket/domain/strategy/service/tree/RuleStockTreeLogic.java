package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import org.springframework.stereotype.Component;

@Component("rule_stock")
public class RuleStockTreeLogic implements ITreeLogic {
    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.TAKE_OVER)
                .build();
    }
}
