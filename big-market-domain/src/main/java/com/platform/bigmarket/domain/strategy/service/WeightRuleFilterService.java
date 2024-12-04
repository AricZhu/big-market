package com.platform.bigmarket.domain.strategy.service;

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
public class WeightRuleFilterService implements IRuleFilterService<BeforeRaffleEntity> {
    /** 这里先写死用户运气值，后续查询 */
    private Integer USER_SCORE = 5500;

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public RuleRaffleEntity<BeforeRaffleEntity> filter(RuleFilterEntity ruleFilterEntity) {
        Long strategyId = ruleFilterEntity.getStrategyId();
        String[] ruleModels = ruleFilterEntity.getStrategyEntity().getRuleModelArray();
        boolean hasRuleWeight = Arrays.stream(ruleModels).anyMatch(item -> RuleModel.WEIGHT.getCode().equals(item));
        if (!hasRuleWeight) {
            return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.WEIGHT.getCode())
                    .build();
        }

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(strategyId, RuleModel.WEIGHT.getCode());

        Integer weight = strategyRuleEntity.calcWeight(USER_SCORE);
        if (weight.equals(-1)) {
            return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.WEIGHT.getCode())
                    .build();
        }

        return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                .ruleActionCode(RuleAction.TAKE_OVER.getCode())
                .ruleModel(RuleModel.WEIGHT.getCode())
                .data(BeforeRaffleEntity.builder()
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
