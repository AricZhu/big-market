package com.platform.bigmarket.domain.strategy.service.tree;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.NodeLineExpressionType;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeLineDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeNodeDTO;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultDecisionEngine implements IDecisionEngine {
    private Map<String, ITreeLogic> treeLogicMap;
    private RuleTreeDTO ruleTreeDTO;

    public DefaultDecisionEngine(Map<String, ITreeLogic> treeLogicMap, RuleTreeDTO ruleTreeDTO) {
        this.treeLogicMap = treeLogicMap;
        this.ruleTreeDTO = ruleTreeDTO;
    }


    @Override
    public DefaultTreeLogicFactory.RuleFilterTreeAwardEntity process(String userId, Long strategyId, Integer awardId) {
        Map<String, RuleTreeNodeDTO> treeNodeMap = ruleTreeDTO.getTreeNodeMap();
        String rootNode = ruleTreeDTO.getRootNode();
        DefaultTreeLogicFactory.RuleFilterTreeAwardEntity awardDataEntity = null;

        RuleTreeNodeDTO nextNode = treeNodeMap.get(rootNode);
        while (null != nextNode) {
            ITreeLogic treeLogic = treeLogicMap.get(nextNode.getRuleKey());
            if (null == treeLogic) {
                throw new BizException(ExceptionCode.ERROR_TREE_RULE_KEY.getCode(), "节点规则 key 配置错误: " + nextNode.getRuleKey());
            }
            DefaultTreeLogicFactory.TreeRuleActionAward logic = treeLogic.logic(userId, strategyId, awardId, nextNode.getRuleValue());
            awardDataEntity = logic.getAwardDataEntity();
            log.info("运行规则过滤: {}， 结果: {}", nextNode.getRuleKey(), JSON.toJSONString(awardDataEntity));
            nextNode = nextTreeNode(logic.getRuleAction().getCode(), nextNode.getRuleTreeLineDTOList());
        }

        return awardDataEntity;
    }

    private RuleTreeNodeDTO nextTreeNode(String targetValue, List<RuleTreeLineDTO> ruleTreeLineDTOList) {
        if (null == ruleTreeLineDTOList || ruleTreeLineDTOList.isEmpty()) {
            return null;
        }

        for (RuleTreeLineDTO ruleTreeLineDTO : ruleTreeLineDTOList) {
            if (doLineExpression(targetValue, ruleTreeLineDTO)) {
                return ruleTreeDTO.getTreeNodeMap().get(ruleTreeLineDTO.getNodeTo());
            }
        }
        return null;
    }

    private boolean doLineExpression(String targetValue, RuleTreeLineDTO ruleTreeLineDTO) {
        if (ruleTreeLineDTO.getLineExpressionType().equals(NodeLineExpressionType.EQUAL.getCode())) {
            return targetValue.equals(ruleTreeLineDTO.getLineExpressionValue());
        }

        return false;
    }
}
