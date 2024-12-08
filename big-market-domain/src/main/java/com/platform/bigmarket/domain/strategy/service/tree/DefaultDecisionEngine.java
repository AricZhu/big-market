package com.platform.bigmarket.domain.strategy.service.tree;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.NodeLineExpressionType;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTree;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeLine;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeNode;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultDecisionEngine implements IDecisionEngine {
    private Map<String, ITreeLogic> treeLogicMap;
    private RuleTree ruleTree;

    public DefaultDecisionEngine(Map<String, ITreeLogic> treeLogicMap, RuleTree ruleTree) {
        this.treeLogicMap = treeLogicMap;
        this.ruleTree = ruleTree;
    }


    @Override
    public DefaultTreeLogicFactory.AwardDataEntity process(String userId, Long strategyId, Integer awardId) {
        Map<String, RuleTreeNode> treeNodeMap = ruleTree.getTreeNodeMap();
        String rootNode = ruleTree.getRootNode();
        DefaultTreeLogicFactory.AwardDataEntity awardDataEntity = null;

        RuleTreeNode nextNode = treeNodeMap.get(rootNode);
        while (null != nextNode) {
            ITreeLogic treeLogic = treeLogicMap.get(nextNode.getRuleKey());
            if (null == treeLogic) {
                throw new BizException(ExceptionCode.ERROR_TREE_RULE_KEY.getCode(), "节点规则 key 配置错误: " + nextNode.getRuleKey());
            }
            DefaultTreeLogicFactory.TreeRuleActionAward logic = treeLogic.logic(userId, strategyId, awardId);
            awardDataEntity = logic.getAwardDataEntity();
            log.info("运行规则过滤: {}， 结果: {}", nextNode.getRuleKey(), JSON.toJSONString(awardDataEntity));
            nextNode = nextTreeNode(logic.getRuleAction().getCode(), nextNode.getRuleTreeLineList());
        }

        return awardDataEntity;
    }

    private RuleTreeNode nextTreeNode(String targetValue, List<RuleTreeLine> ruleTreeLineList) {
        if (null == ruleTreeLineList || ruleTreeLineList.isEmpty()) {
            return null;
        }

        for (RuleTreeLine ruleTreeLine : ruleTreeLineList) {
            if (doLineExpression(targetValue, ruleTreeLine)) {
                return ruleTree.getTreeNodeMap().get(ruleTreeLine.getNodeTo());
            }
        }
        return null;
    }

    private boolean doLineExpression(String targetValue, RuleTreeLine ruleTreeLine) {
        if (ruleTreeLine.getLineExpressionType().getCode().equals(NodeLineExpressionType.EQUAL.getCode())) {
            return targetValue.equals(ruleTreeLine.getLineExpressionValue().getCode());
        }

        return false;
    }
}
