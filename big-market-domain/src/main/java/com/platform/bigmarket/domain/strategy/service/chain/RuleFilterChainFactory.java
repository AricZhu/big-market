package com.platform.bigmarket.domain.strategy.service.chain;

import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleFilterChainFactory {
    @Autowired
    private Map<String, IRuleFilterChain> ruleFilterChainMap;

    @Autowired
    private IStrategyRepository strategyRepository;

    private Map<Long, IRuleFilterChain> ruleFilterChainCache = new HashMap<>();

    /**
     * 开始装配责任链
     * @param strategyId
     * @return
     */
    public IRuleFilterChain openRuleFilterChain(Long strategyId) {
        if (ruleFilterChainCache.containsKey(strategyId)) {
            return ruleFilterChainCache.get(strategyId);
        }

        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntity(strategyId);
        String[] ruleModelArray = strategyEntity.getRuleModelArray();

        // 没有规则时，直接装配默认过滤
        if (ruleModelArray.length == 0) {
            IRuleFilterChain ruleFilterChain = ruleFilterChainMap.get(RuleModel.DEFAULT.getCode());
            ruleFilterChainCache.put(strategyId, ruleFilterChain);
            return ruleFilterChain;
        }

        IRuleFilterChain preRuleFilterChain = null;
        IRuleFilterChain firstRuleFilterChain = null;
        for (String ruleModel : ruleModelArray) {
            IRuleFilterChain ruleFilterChain = ruleFilterChainMap.get(ruleModel);

            if (null == preRuleFilterChain) {
                preRuleFilterChain = ruleFilterChain;
                firstRuleFilterChain = ruleFilterChain;
            } else {
                preRuleFilterChain = preRuleFilterChain.appendNext(ruleFilterChain);
            }
        }

        IRuleFilterChain defaultChain = ruleFilterChainMap.get(RuleModel.DEFAULT.getCode());
        preRuleFilterChain.appendNext(defaultChain);

        ruleFilterChainCache.put(strategyId, firstRuleFilterChain);
        return firstRuleFilterChain;
    }
}
