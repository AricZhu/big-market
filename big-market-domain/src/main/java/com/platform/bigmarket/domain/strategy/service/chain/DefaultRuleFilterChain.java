package com.platform.bigmarket.domain.strategy.service.chain;

import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RuleFilterEntity;
import com.platform.bigmarket.domain.strategy.service.IStrategyLottery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component("default")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultRuleFilterChain extends AbstractRuleFilterChain {
    @Autowired
    private IStrategyLottery strategyLottery;

    @Override
    public RaffleAwardEntity filter(RuleFilterEntity ruleFilterEntity) {
        log.info("开始执行默认抽奖");
        Integer awardId = strategyLottery.doLottery(ruleFilterEntity.getStrategyId());

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }
}
