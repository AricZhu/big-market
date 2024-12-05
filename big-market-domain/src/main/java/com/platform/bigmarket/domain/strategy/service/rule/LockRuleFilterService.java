package com.platform.bigmarket.domain.strategy.service.rule;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.CenterRaffleActionEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleRaffleEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockRuleFilterService implements IRuleFilterService<CenterRaffleActionEntity> {
    // 先写死用户抽奖次数，后面从 redis 中读取
    public Integer userRaffleCount = 0;

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public RuleRaffleEntity<CenterRaffleActionEntity> filter(RuleFilterEntity ruleFilterEntity) {
        Long strategyId = ruleFilterEntity.getStrategyId();

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(strategyId, RuleModel.RULE_LOCK.getCode(), ruleFilterEntity.getAwardId());
        if (null == strategyRuleEntity) {
            return RuleRaffleEntity.<CenterRaffleActionEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.RULE_LOCK.getCode())
                    .build();
        }

        Integer lockCount = Integer.valueOf(strategyRuleEntity.getRuleValue());

        // 未达到解锁次数
        if (userRaffleCount < lockCount) {
            return RuleRaffleEntity.<CenterRaffleActionEntity>builder()
                    .ruleActionCode(RuleAction.TAKE_OVER.getCode())
                    .ruleModel(RuleModel.RULE_LOCK.getCode())
                    .build();
        }

        return RuleRaffleEntity.<CenterRaffleActionEntity>builder()
                .ruleActionCode(RuleAction.ALLOW.getCode())
                .ruleModel(RuleModel.RULE_LOCK.getCode())
                .build();
    }

    @Override
    public String[] getSupportRuleModel() {
        return new String[]{RuleModel.RULE_LOCK.getCode()};
    }
}
