package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.BeforeRaffleEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleRaffleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRaffleService extends AbstractRaffleService {
    @Autowired
    private RuleFilterFactory ruleFilterFactory;

    @Override
    protected RuleRaffleEntity<BeforeRaffleEntity> doBeforeRaffleRuleFilter(RuleFilterEntity ruleFilterEntity) {
        // 先做黑名单过滤
        IRuleFilterService<BeforeRaffleEntity> blackListRuleFilter = ruleFilterFactory.getRuleFilterByRuleModel(RuleModel.BLACK_LIST.getCode());
        RuleRaffleEntity<BeforeRaffleEntity> filter = blackListRuleFilter.filter(ruleFilterEntity);
        if (RuleAction.TAKE_OVER.getCode().equals(filter.getRuleActionCode())) {
            return filter;
        }

        for (String ruleModel : ruleFilterEntity.getStrategyEntity().getRuleModelArray()) {
            if (RuleModel.BLACK_LIST.getCode().equals(ruleModel)) {
                continue;
            }

            IRuleFilterService<BeforeRaffleEntity> ruleFilter = ruleFilterFactory.getRuleFilterByRuleModel(ruleModel);
            RuleRaffleEntity<BeforeRaffleEntity> filterResult = ruleFilter.filter(ruleFilterEntity);
            if (RuleAction.TAKE_OVER.getCode().equals(filterResult.getRuleActionCode())) {
                return filterResult;
            }
        }

        // 其他过滤
        return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                .ruleActionCode(RuleAction.ALLOW.getCode())
                .build();
    }
}
