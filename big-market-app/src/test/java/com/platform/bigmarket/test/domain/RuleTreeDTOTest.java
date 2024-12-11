package com.platform.bigmarket.test.domain;

import com.alibaba.fastjson2.JSON;
import com.platform.bigmarket.domain.strategy.model.common.NodeLineExpressionType;
import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeLineDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeNodeDTO;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
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
public class RuleTreeDTOTest {

    @Autowired
    private DefaultTreeLogicFactory defaultTreeLogicFactory;

    @Test
    public void test_ruleTree() {
        RuleTreeNodeDTO rule_lock = RuleTreeNodeDTO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .ruleTreeLineDTOList(new ArrayList<RuleTreeLineDTO>() {{
                    add(RuleTreeLineDTO.builder()
                            .treeId("tree_lock")
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_luck_award")
                            .lineExpressionType(NodeLineExpressionType.EQUAL.getCode())
                            .lineExpressionValue(RuleAction.TAKE_OVER.getCode())
                            .build());

                    add(RuleTreeLineDTO.builder()
                            .treeId("tree_lock")
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_stock")
                            .lineExpressionType(NodeLineExpressionType.EQUAL.getCode())
                            .lineExpressionValue(RuleAction.ALLOW.getCode())
                            .build());
                }})
                .build();

        RuleTreeNodeDTO rule_luck_award = RuleTreeNodeDTO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .ruleTreeLineDTOList(null)
                .build();

        RuleTreeNodeDTO rule_stock = RuleTreeNodeDTO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .ruleTreeLineDTOList(new ArrayList<RuleTreeLineDTO>() {{
                    add(RuleTreeLineDTO.builder()
                            .treeId("tree_lock")
                            .nodeFrom("rule_lock")
                            .nodeTo("rule_luck_award")
                            .lineExpressionType(NodeLineExpressionType.EQUAL.getCode())
                            .lineExpressionValue(RuleAction.TAKE_OVER.getCode())
                            .build());
                }})
                .build();

        RuleTreeDTO ruleTreeDTOVO = new RuleTreeDTO();
        ruleTreeDTOVO.setTreeId("tree_lock");
        ruleTreeDTOVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeDTOVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        ruleTreeDTOVO.setRootNode("rule_lock");

        ruleTreeDTOVO.setTreeNodeMap(new HashMap<String, RuleTreeNodeDTO>() {{
            put("rule_lock", rule_lock);
            put("rule_stock", rule_stock);
            put("rule_luck_award", rule_luck_award);
        }});

        IDecisionEngine treeEngine = defaultTreeLogicFactory.openTreeLogicEngine(ruleTreeDTOVO);

        DefaultTreeLogicFactory.RuleFilterTreeAwardEntity data = treeEngine.process("xiaofuge", 100001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));
    }
}
