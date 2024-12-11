package com.platform.bigmarket.infrastructure.persistent.repository;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeLineDTO;
import com.platform.bigmarket.domain.strategy.model.valobj.RuleTreeNodeDTO;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.infrastructure.persistent.dao.*;
import com.platform.bigmarket.infrastructure.persistent.po.*;
import com.platform.bigmarket.infrastructure.persistent.redis.IRedisService;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {
    private static final Map<String, Object> CacheData = new HashMap<>();
    private final String RuleTreePrefix = "rule_tree_";

    @Autowired
    private IStrategyAwardDao strategyAwardDao;

    @Autowired
    private IStrategyRuleDao strategyRuleDao;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IStrategyDao strategyDao;

    @Autowired
    private IRuleTreeDao ruleTreeDao;

    @Autowired
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Autowired
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId) {
        List<StrategyAward> strategyAwardList = strategyAwardDao.queryStrategyAwardListById(strategyId);

        ArrayList<StrategyAwardEntity> strategyAwardEntityList = new ArrayList<>();

        for (StrategyAward strategyAward : strategyAwardList) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntityList.add(strategyAwardEntity);
        }

        return strategyAwardEntityList;
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = strategyAwardDao.queryStrategyAwardById(strategyId, awardId);
        if (null != strategyAward) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            return strategyAwardEntity;
        }
        return null;
    }

    @Override
    public StrategyEntity queryStrategyEntity(Long strategyId) {
        Strategy strategy = strategyDao.queryStrategy(strategyId);
        if (null == strategy) {
            throw new BizException(ExceptionCode.ILLEGAL_PARAMS.getCode(), "策略 id=" + strategyId + " 查询为空");
        }
        StrategyEntity strategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, strategyEntity);

        return strategyEntity;
    }

    @Override
    public Map<String, Integer> getStrategyAwardRateTable(String key) {
        if (CacheData.containsKey(key)) {
            return (Map<String, Integer>) CacheData.get(key);
        }

        Map<String, Integer> value = (Map<String, Integer>) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStrategyAwardRateTable(String key, Map<String, Integer> rateTable) {
        redisService.setValue(key, rateTable);
    }

    @Override
    public Integer getStragetyAwardRange(String key) {
        if (CacheData.containsKey(key)) {
            return (Integer) CacheData.get(key);
        }

        Integer value = (Integer) redisService.getValue(key);
        CacheData.put(key, value);

        return value;
    }

    @Override
    public void setStragetyAwardRange(String key, Integer range) {
        redisService.setValue(key, range);
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel, Integer awardId) {
        StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyId, ruleModel, awardId);
        if (null == strategyRule) {
            return null;
        }
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        BeanUtils.copyProperties(strategyRule, strategyRuleEntity);

        return strategyRuleEntity;
    }

    @Override
    public RuleTreeDTO getRuleTree(String treeId) {
        RuleTreeDTO ruleTreeDTO = redisService.<RuleTreeDTO>getValue(RuleTreePrefix + treeId);
        if (ruleTreeDTO != null) {
            return ruleTreeDTO;
        }

        RuleTreeDTO ruleTreeDTO1 = this.buildRuleTree(treeId);
        redisService.setValue(RuleTreePrefix + treeId, ruleTreeDTO1);

        return ruleTreeDTO1;
    }

    private RuleTreeDTO buildRuleTree(String treeId) {
        RuleTree ruleTreeDo = ruleTreeDao.queryRuleTree(treeId);

        if (ruleTreeDo == null) {
            throw new BizException(ExceptionCode.NOT_FOUND_RULE_TREE.getCode(), "找不到规则树: " + treeId);
        }
        // 构造树根节点
        RuleTreeDTO ruleTreeDTO = RuleTreeDTO.builder()
                .treeId(treeId)
                .treeName(ruleTreeDo.getTreeName())
                .treeDesc(ruleTreeDo.getTreeDesc())
                .rootNode(ruleTreeDo.getTreeNodeRuleKey())
                .build();

        // 构建节点
        Map<String, RuleTreeNodeDTO> ruleTreeNodeMap = new HashMap<>();
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeDao.queryRuleTreeNodeList(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineList(treeId);

        for (RuleTreeNode ruleTreeNode : ruleTreeNodeList) {
            RuleTreeNodeDTO ruleTreeNodeDTO = RuleTreeNodeDTO.builder()
                    .treeId(treeId)
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleTreeLineDTOList(ruleTreeNodeLines.stream()
                            .filter(line -> line.getRuleNodeFrom().equals(ruleTreeNode.getRuleKey()))
                            .map(line -> {
                                return RuleTreeLineDTO.builder()
                                        .treeId(treeId)
                                        .nodeFrom(line.getRuleNodeFrom())
                                        .nodeTo(line.getRuleNodeTo())
                                        .lineExpressionType(line.getRuleLimitType())
                                        .lineExpressionValue(line.getRuleLimitValue())
                                        .build();
                            })
                            .toList())
                    .build();

            ruleTreeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeDTO);
        }

        ruleTreeDTO.setTreeNodeMap(ruleTreeNodeMap);
        return ruleTreeDTO;
    }
}
