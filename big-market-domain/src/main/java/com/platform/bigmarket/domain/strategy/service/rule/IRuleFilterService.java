package com.platform.bigmarket.domain.strategy.service.rule;

import com.platform.bigmarket.domain.strategy.model.entity.RaffleActionEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleRaffleEntity;

public interface IRuleFilterService<T extends RaffleActionEntity> {
    RuleRaffleEntity<T> filter(RuleFilterEntity ruleFilterEntity);

    String[] getSupportRuleModel();
}
