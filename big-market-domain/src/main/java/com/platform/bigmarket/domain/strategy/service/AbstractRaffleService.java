package com.platform.bigmarket.domain.strategy.service;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
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


    @Override
    public RaffleAwardEntity performRaffle(RaffleParamsEntity raffleParams) {
        if (StringUtils.isBlank(raffleParams.getUserId()) || raffleParams.getStrategyId() == null) {
            throw new BizException(ExceptionCode.ILLEGAL_PARAMS);
        }

        // 1. 查询抽奖规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntity(raffleParams.getStrategyId());

        // 2. 进行规则过滤
        RuleRaffleEntity<BeforeRaffleEntity> ruleRaffleEntity = this.doBeforeRaffleRuleFilter(
                RuleFilterEntity.builder()
                        .userId(raffleParams.getUserId())
                        .strategyId(raffleParams.getStrategyId())
                        .strategyEntity(strategyEntity)
                .build());

        // 3. 接管抽奖
        if (RuleAction.TAKE_OVER.getCode().equals(ruleRaffleEntity.getRuleActionCode())) {
            // 黑名单抽奖
            if (RuleModel.BLACK_LIST.getCode().equals(ruleRaffleEntity.getRuleModel())) {
                log.info("执行黑名单抽奖：{}", JSON.toJSONString(ruleRaffleEntity));
                return RaffleAwardEntity.builder()
                        .awardId(ruleRaffleEntity.getData().getAwardId())
                        .build();
            }

            // 权重抽奖
            if (RuleModel.WEIGHT.getCode().equals(ruleRaffleEntity.getRuleModel())) {
                log.info("执行权重抽奖：{}", JSON.toJSONString(ruleRaffleEntity));
                Integer awardId = strategyLottery.doLotteryByWeight(ruleRaffleEntity.getData().getStrategyId(), ruleRaffleEntity.getData().getWeight());
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        // 正常抽奖
        Integer awardId = strategyLottery.doLottery(raffleParams.getStrategyId());

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleRaffleEntity<BeforeRaffleEntity> doBeforeRaffleRuleFilter(RuleFilterEntity ruleFilterEntity);
}