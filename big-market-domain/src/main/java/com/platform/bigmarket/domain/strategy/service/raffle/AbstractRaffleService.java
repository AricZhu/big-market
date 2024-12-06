package com.platform.bigmarket.domain.strategy.service.raffle;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.IStrategyLottery;
import com.platform.bigmarket.domain.strategy.service.chain.IRuleFilterChain;
import com.platform.bigmarket.domain.strategy.service.chain.RuleFilterChainFactory;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽奖抽象类，定义抽奖的流程
 */
@Slf4j
public abstract class AbstractRaffleService implements IRaffleService {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Autowired
    private IStrategyLottery strategyLottery;

    @Autowired
    private RuleFilterChainFactory ruleFilterChainFactory;


    @Override
    public RaffleAwardEntity performRaffle(RaffleParamsEntity raffleParams) {
        if (StringUtils.isBlank(raffleParams.getUserId()) || raffleParams.getStrategyId() == null) {
            throw new BizException(ExceptionCode.ILLEGAL_PARAMS);
        }

        // 1. 查询抽奖规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntity(raffleParams.getStrategyId());

        RuleFilterEntity ruleFilterEntity = RuleFilterEntity.builder()
                .userId(raffleParams.getUserId())
                .strategyId(raffleParams.getStrategyId())
                .strategyEntity(strategyEntity)
                .build();

        IRuleFilterChain iRuleFilterChain = ruleFilterChainFactory.openRuleFilterChain(raffleParams.getStrategyId());
        RaffleAwardEntity raffleAwardEntity = iRuleFilterChain.filter(ruleFilterEntity);

        // 抽奖中-规则过滤
        RuleRaffleEntity<CenterRaffleActionEntity> centerRuleRaffleEntity = this.doCenterRaffleRuleFilter(RuleFilterEntity.builder()
                .userId(raffleParams.getUserId())
                .strategyId(raffleParams.getStrategyId())
                .awardId(raffleAwardEntity.getAwardId())
                .strategyEntity(strategyEntity)
                .build());
        if (RuleAction.TAKE_OVER.getCode().equals(centerRuleRaffleEntity.getRuleActionCode())) {
            return RaffleAwardEntity.builder()
                    .awardId(101)
                    .awardTitle("未满足抽奖次数，派发兜底奖品")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .build();
    }

    protected abstract RuleRaffleEntity<BeforeRaffleActionEntity> doBeforeRaffleRuleFilter(RuleFilterEntity ruleFilterEntity);
    protected abstract RuleRaffleEntity<CenterRaffleActionEntity> doCenterRaffleRuleFilter(RuleFilterEntity ruleFilterEntity);
}
