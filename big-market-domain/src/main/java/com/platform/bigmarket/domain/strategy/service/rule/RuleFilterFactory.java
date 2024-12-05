package com.platform.bigmarket.domain.strategy.service.rule;

import com.platform.bigmarket.domain.strategy.model.entity.BeforeRaffleActionEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleActionEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RuleFilterFactory {
    @Autowired
    List<IRuleFilterService<? extends RaffleActionEntity>> ruleFilterServiceList;

    private final Map<String, IRuleFilterService<? extends RaffleActionEntity>> ruleFilterServiceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IRuleFilterService<? extends RaffleActionEntity> iRuleFilterService : ruleFilterServiceList) {
            String[] supportRuleModel = iRuleFilterService.getSupportRuleModel();
            for (String ruleModel : supportRuleModel) {
                ruleFilterServiceMap.put(ruleModel, iRuleFilterService);
            }
        }
    }

    public IRuleFilterService<? extends RaffleActionEntity> getRuleFilterByRuleModel(String ruleModel) {
        return ruleFilterServiceMap.get(ruleModel);
    }
}
