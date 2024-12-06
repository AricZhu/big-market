package com.platform.bigmarket.domain.strategy.service.chain;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.IStrategyLottery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Slf4j
@Component("rule_weight")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RuleWeightRuleFilterChain extends AbstractRuleFilterChain {
    public Integer USER_SCORE = 5500;


    @Autowired
    private IStrategyRepository strategyRepository;

    @Autowired
    private IStrategyLottery strategyLottery;

    @Override
    public RaffleAwardEntity filter(RuleFilterEntity ruleFilterEntity) {
        log.info("开始权重责任链过滤: {}", JSON.toJSONString(ruleFilterEntity));

        Long strategyId = ruleFilterEntity.getStrategyId();

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(strategyId, RuleModel.WEIGHT.getCode(), null);

        Integer weight = strategyRuleEntity.calcWeight(USER_SCORE);

        if (weight.equals(-1)) {
            log.info("权重责任链未命中，开始下一个责任链");
            return next().filter(ruleFilterEntity);
        }

        log.info("权重责任链命中，开始执行权重抽奖: {}", weight);
        Integer awardId = strategyLottery.doLotteryByWeight(strategyId, weight);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }
}
