package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.BeforeRaffleEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleRaffleEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 黑名单过滤
 */
@Service
public class BlackListRuleFilterService implements IRuleFilterService<BeforeRaffleEntity> {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public RuleRaffleEntity<BeforeRaffleEntity> filter(RuleFilterEntity ruleFilterEntity) {
        String[] ruleModels = ruleFilterEntity.getStrategyEntity().getRuleModelArray();
        boolean hasBlackListModel = Arrays.stream(ruleModels).anyMatch(item -> RuleModel.BLACK_LIST.getCode().equals(item));
        // 没有黑名单规则，直接放行
        if (!hasBlackListModel) {
            return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                    .ruleActionCode(RuleAction.ALLOW.getCode())
                    .ruleModel(RuleModel.BLACK_LIST.getCode())
                    .build();
        }

        // 判断当前用户是否是黑名单用户
        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(ruleFilterEntity.getStrategyId(), RuleModel.BLACK_LIST.getCode());
        String ruleValue = strategyRuleEntity.getRuleValue();
        String[] ruleValueArr = ruleValue.split(":");
        String[] blackUserList = ruleValueArr[1].split(",");
        if (Arrays.stream(blackUserList).anyMatch(item -> item.equals(ruleFilterEntity.getUserId()))) {
            return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                    .ruleActionCode(RuleAction.TAKE_OVER.getCode())
                    .ruleModel(RuleModel.BLACK_LIST.getCode())
                    .data(BeforeRaffleEntity.builder()
                            .strategyId(ruleFilterEntity.getStrategyId())
                            .awardId(Integer.valueOf(ruleValueArr[0]))
                            .build())
                    .build();
        }

        return RuleRaffleEntity.<BeforeRaffleEntity>builder()
                .ruleActionCode(RuleAction.ALLOW.getCode())
                .ruleModel(RuleModel.BLACK_LIST.getCode())
                .build();
    }

    @Override
    public String[] getSupportRuleModel() {
        return new String[]{RuleModel.BLACK_LIST.getCode()};
    }
}
