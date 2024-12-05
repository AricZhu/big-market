package com.platform.bigmarket.domain.strategy.service.rule;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 权重规则过滤
 */
@Service
public class WeightRuleFilterService implements IRuleFilterService<BeforeRaffleActionEntity> {
    /** 这里先写死用户运气值，后续查询 */
    public Integer USER_SCORE = 5500;

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public RuleRaffleEntity<BeforeRaffleActionEntity> filter(RuleFilterEntity ruleFilterEntity) {
        Long strategyId = ruleFilterEntity.getStrategyId();
        String[] ruleModels = ruleFilterEntity.getStrategyEntity().getRuleModelArray();
        boolean hasRuleWeight = Arrays.stream(ruleModels).anyMatch(item -> RuleModel.WEIGHT.getCode().equals(item));
        if (!hasRuleWeight) {
            return RuleRaffleEntity.<BeforeRaffleActionEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.WEIGHT.getCode())
                    .build();
        }

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(strategyId, RuleModel.WEIGHT.getCode(), null);

        Integer weight = strategyRuleEntity.calcWeight(USER_SCORE);
        if (weight.equals(-1)) {
            return RuleRaffleEntity.<BeforeRaffleActionEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.WEIGHT.getCode())
                    .build();
        }

        return RuleRaffleEntity.<BeforeRaffleActionEntity>builder()
                .ruleActionCode(RuleAction.TAKE_OVER.getCode())
                .ruleModel(RuleModel.WEIGHT.getCode())
                .data(BeforeRaffleActionEntity.builder()
                        .strategyId(strategyId)
                        .weight(weight)
                        .build())
                .build();
    }

    @Override
    public String[] getSupportRuleModel() {
        return new String[] {RuleModel.WEIGHT.getCode()};
    }
}
