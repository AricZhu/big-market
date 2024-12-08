package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import org.springframework.stereotype.Component;

@Component("rule_luck_award")
public class RuleLuckDrawTreeLogic implements ITreeLogic {
    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.TAKE_OVER)
                .awardDataEntity(DefaultTreeLogicFactory.AwardDataEntity.builder()
                        .awardId(101)
                        .awardValue("1:100")
                        .build())
                .build();
    }
}
