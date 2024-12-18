package com.platform.bigmarket.infrastructure.persistent.dao;

import com.platform.bigmarket.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(@Param("strategyId") Long strategyId, @Param("ruleModel") String ruleModel, @Param("awardId") Integer awardId);
}
