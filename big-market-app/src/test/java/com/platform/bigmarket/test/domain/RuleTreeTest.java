package com.platform.bigmarket.test.domain;

import com.alibaba.fastjson2.JSON;
import com.platform.bigmarket.domain.strategy.model.common.NodeLineExpressionType;
import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTree;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeLine;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeNode;
import com.platform.bigmarket.domain.strategy.service.tree.DefaultTreeLogicFactory;
import com.platform.bigmarket.domain.strategy.service.tree.IDecisionEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RuleTreeTest {

    @Autowired
    private DefaultTreeLogicFactory defaultTreeLogicFactory;

    @Test
    public void test_ruleTree() {
        RuleTreeNode rule_lock = RuleTreeNode.builder()
                .treeId(100000001)
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .ruleTreeLineList(new ArrayList<RuleTreeLine>() {{
                    add(RuleTreeLine.builder()
                            .treeId(100000001)
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_luck_award")
                            .lineExpressionType(NodeLineExpressionType.EQUAL)
                            .lineExpressionValue(RuleAction.TAKE_OVER)
                            .build());

                    add(RuleTreeLine.builder()
                            .treeId(100000001)
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_stock")
                            .lineExpressionType(NodeLineExpressionType.EQUAL)
                            .lineExpressionValue(RuleAction.ALLOW)
                            .build());
                }})
                .build();

        RuleTreeNode rule_luck_award = RuleTreeNode.builder()
                .treeId(100000001)
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .ruleTreeLineList(null)
                .build();

        RuleTreeNode rule_stock = RuleTreeNode.builder()
                .treeId(100000001)
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .ruleTreeLineList(new ArrayList<RuleTreeLine>() {{
                    add(RuleTreeLine.builder()
                            .treeId(100000001)
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_luck_award")
                            .lineExpressionType(NodeLineExpressionType.EQUAL)
                            .lineExpressionValue(RuleAction.TAKE_OVER)
                            .build());
                }})
                .build();

        RuleTree ruleTreeVO = new RuleTree();
        ruleTreeVO.setTreeId(100000001);
        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setRootNode("rule_lock");

        ruleTreeVO.setTreeNodeMap(new HashMap<String, RuleTreeNode>() {{
            put("rule_lock", rule_lock);
            put("rule_stock", rule_stock);
            put("rule_luck_award", rule_luck_award);
        }});

        IDecisionEngine treeEngine = defaultTreeLogicFactory.openTreeLogicEngine(ruleTreeVO);

        DefaultTreeLogicFactory.AwardDataEntity data = treeEngine.process("xiaofuge", 100001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));
    }
}
