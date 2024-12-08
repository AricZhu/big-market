package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTree;
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

    public IDecisionEngine openTreeLogicEngine(RuleTree ruleTree) {
        return new DefaultDecisionEngine(this.treeLogicMap, ruleTree);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeRuleActionAward {
        private RuleAction ruleAction;
        private AwardDataEntity awardDataEntity;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AwardDataEntity {
        private Integer awardId;
        private String awardValue;
    }
}
