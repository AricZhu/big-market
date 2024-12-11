package com.platform.bigmarket.infrastructure.persistent.dao;

import com.platform.bigmarket.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryRuleTreeNodeList(String treeId);
}
