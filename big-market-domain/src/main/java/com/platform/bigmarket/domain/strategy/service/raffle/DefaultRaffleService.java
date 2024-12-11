package com.platform.bigmarket.domain.strategy.service.raffle;

import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.chain.IRuleFilterChain;
import com.platform.bigmarket.domain.strategy.service.chain.factory.RuleFilterChainFactory;
import com.platform.bigmarket.domain.strategy.service.tree.IDecisionEngine;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRaffleService extends AbstractRaffleService {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Autowired
    private RuleFilterChainFactory ruleFilterChainFactory;

    @Autowired
    private DefaultTreeLogicFactory treeLogicFactory;

    @Override
    protected RuleFilterChainFactory.RuleFilterChainAwardEntity doLogicChainFilter(RaffleParamsEntity raffleParams) {// 1. 查询抽奖规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntity(raffleParams.getStrategyId());

        RuleFilterEntity ruleFilterEntity = RuleFilterEntity.builder()
                .userId(raffleParams.getUserId())
                .strategyId(raffleParams.getStrategyId())
                .strategyEntity(strategyEntity)
                .build();

        IRuleFilterChain iRuleFilterChain = ruleFilterChainFactory.openRuleFilterChain(raffleParams.getStrategyId());
        RuleFilterChainFactory.RuleFilterChainAwardEntity raffleAwardEntity = iRuleFilterChain.filter(ruleFilterEntity);
        return raffleAwardEntity;
    }

    @Override
    protected DefaultTreeLogicFactory.RuleFilterTreeAwardEntity doLogicTreeFilter(RaffleTreeParamsEntity raffleTreeParamsEntity) {
        String userId = raffleTreeParamsEntity.getUserId();
        Long strategyId = raffleTreeParamsEntity.getStrategyId();
        Integer awardId = raffleTreeParamsEntity.getAwardId();

        StrategyAwardEntity strategyAwardEntity = strategyRepository.queryStrategyAwardEntity(strategyId, awardId);

        // 当前奖品没有规则树过滤
        if (strategyAwardEntity == null) {
            return DefaultTreeLogicFactory.RuleFilterTreeAwardEntity.builder()
                    .awardId(awardId)
                    .build();
        }

        RuleTreeDTO ruleTreeDTO = strategyRepository.getRuleTree(strategyAwardEntity.getRuleModels());
        if (ruleTreeDTO == null) {
            return DefaultTreeLogicFactory.RuleFilterTreeAwardEntity.builder()
                    .awardId(awardId)
                    .build();
        }

        IDecisionEngine decisionEngine = treeLogicFactory.openTreeLogicEngine(ruleTreeDTO);
        return decisionEngine.process(userId, strategyId, awardId);
    }
}
