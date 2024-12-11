package com.platform.bigmarket.infrastructure.persistent.dao;

import com.platform.bigmarket.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTree(String treeId);
}
