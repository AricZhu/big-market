package com.platform.bigmarket.domain.strategy.service.raffle;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.service.rule.IRuleFilterService;
import com.platform.bigmarket.domain.strategy.service.rule.RuleFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRaffleService extends AbstractRaffleService {
    @Autowired
    private RuleFilterFactory ruleFilterFactory;

    @Override
    protected RuleRaffleEntity<BeforeRaffleActionEntity> doBeforeRaffleRuleFilter(RuleFilterEntity ruleFilterEntity) {
        // 先做黑名单过滤
        IRuleFilterService<BeforeRaffleActionEntity> blackListRuleFilter = (IRuleFilterService<BeforeRaffleActionEntity>)ruleFilterFactory.getRuleFilterByRuleModel(RuleModel.BLACK_LIST.getCode());
        RuleRaffleEntity<BeforeRaffleActionEntity> filter = blackListRuleFilter.filter(ruleFilterEntity);
        if (RuleAction.TAKE_OVER.getCode().equals(filter.getRuleActionCode())) {
            return filter;
        }

        for (String ruleModel : ruleFilterEntity.getStrategyEntity().getRuleModelArray()) {
            if (RuleModel.BLACK_LIST.getCode().equals(ruleModel)) {
                continue;
            }

            IRuleFilterService<BeforeRaffleActionEntity> ruleFilter = (IRuleFilterService<BeforeRaffleActionEntity>)ruleFilterFactory.getRuleFilterByRuleModel(ruleModel);
            RuleRaffleEntity<BeforeRaffleActionEntity> filterResult = ruleFilter.filter(ruleFilterEntity);
            if (RuleAction.TAKE_OVER.getCode().equals(filterResult.getRuleActionCode())) {
                return filterResult;
            }
        }

        // 其他过滤
        return RuleRaffleEntity.<BeforeRaffleActionEntity>builder()
                .ruleActionCode(RuleAction.ALLOW.getCode())
                .build();
    }

    @Override
    protected RuleRaffleEntity<CenterRaffleActionEntity> doCenterRaffleRuleFilter(RuleFilterEntity ruleFilterEntity) {
        IRuleFilterService<CenterRaffleActionEntity> lockRuleFilter = (IRuleFilterService<CenterRaffleActionEntity>)ruleFilterFactory.getRuleFilterByRuleModel(RuleModel.RULE_LOCK.getCode());
        return lockRuleFilter.filter(ruleFilterEntity);
    }
}
