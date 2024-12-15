package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckDrawTreeLogic implements ITreeLogic {
    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

        String[] split = ruleValue.split(":");
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.TAKE_OVER)
                .awardDataEntity(DefaultTreeLogicFactory.RuleFilterTreeAwardEntity.builder()
                        .awardId(Integer.valueOf(split[0]))
                        .awardValue(split[1])
                        .build())
                .build();
    }
}
