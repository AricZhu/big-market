package com.platform.bigmarket.domain.strategy.service.tree.factory;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.service.tree.DefaultDecisionEngine;
import com.platform.bigmarket.domain.strategy.service.tree.IDecisionEngine;
import com.platform.bigmarket.domain.strategy.service.tree.ITreeLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTreeLogicFactory {
    @Autowired
    private Map<String, ITreeLogic> treeLogicMap;

    public IDecisionEngine openTreeLogicEngine(RuleTreeDTO ruleTreeDTO) {
        return new DefaultDecisionEngine(this.treeLogicMap, ruleTreeDTO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeRuleActionAward {
        private RuleAction ruleAction;
        private RuleFilterTreeAwardEntity awardDataEntity;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RuleFilterTreeAwardEntity {
        private Integer awardId;
        private String awardValue;
    }
}
