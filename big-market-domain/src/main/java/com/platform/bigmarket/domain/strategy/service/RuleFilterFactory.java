package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.entity.BeforeRaffleEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RuleFilterFactory {
    @Autowired
    List<IRuleFilterService<BeforeRaffleEntity>> ruleFilterServiceList;

    private final Map<String, IRuleFilterService<BeforeRaffleEntity>> ruleFilterServiceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IRuleFilterService<BeforeRaffleEntity> iRuleFilterService : ruleFilterServiceList) {
            String[] supportRuleModel = iRuleFilterService.getSupportRuleModel();
            for (String ruleModel : supportRuleModel) {
                ruleFilterServiceMap.put(ruleModel, iRuleFilterService);
            }
        }
    }

    public IRuleFilterService<BeforeRaffleEntity> getRuleFilterByRuleModel(String ruleModel) {
        return ruleFilterServiceMap.get(ruleModel);
    }
}
