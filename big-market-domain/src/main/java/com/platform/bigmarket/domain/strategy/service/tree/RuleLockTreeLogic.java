package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_lock")
public class RuleLockTreeLogic implements ITreeLogic {
    public Integer userRaffleCount = 2;

    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        Integer raffleCount = Integer.valueOf(ruleValue);
        log.info("规则过滤-次数过滤 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

        if (userRaffleCount >= raffleCount) {
            log.info("规则过滤-次数过滤-成功 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

            return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                    .ruleAction(RuleAction.ALLOW)
                    .awardDataEntity(DefaultTreeLogicFactory.RuleFilterTreeAwardEntity.builder()
                            .awardId(awardId)
                            .awardValue(ruleValue)
                            .build())
                    .build();
        }

        log.info("规则过滤-次数过滤-失败! userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.TAKE_OVER)
                .build();
    }
}
