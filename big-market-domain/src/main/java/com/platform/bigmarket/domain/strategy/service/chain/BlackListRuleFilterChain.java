package com.platform.bigmarket.domain.strategy.service.chain;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.chain.factory.RuleFilterChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component("rule_blacklist")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlackListRuleFilterChain extends AbstractRuleFilterChain {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public RuleFilterChainFactory.RuleFilterChainAwardEntity filter(RuleFilterEntity ruleFilterEntity) {
        log.info("开始黑名单责任链过滤: {}", JSON.toJSONString(ruleFilterEntity));
        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(ruleFilterEntity.getStrategyId(), RuleModel.BLACK_LIST.getCode(), null);
        String ruleValue = strategyRuleEntity.getRuleValue();
        String[] ruleValueArr = ruleValue.split(":");
        String[] blackUserList = ruleValueArr[1].split(",");

        // 黑名单用户
        if (Arrays.stream(blackUserList).anyMatch(item -> item.equals(ruleFilterEntity.getUserId()))) {
            log.info("黑名单责任链命中");
            return RuleFilterChainFactory.RuleFilterChainAwardEntity.builder()
                    .ruleModel(RuleModel.BLACK_LIST)
                    .awardId(Integer.valueOf(ruleValueArr[0]))
                    .build();
        }

        // 继续下一个责任链
        log.info("黑名单责任链放行，开启下一个责任链");
        return next().filter(ruleFilterEntity);
    }
}
