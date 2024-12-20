package com.platform.bigmarket.infrastructure.persistent.dao;

import com.platform.bigmarket.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListById(Long strategyId);

    StrategyAward queryStrategyAwardById(@Param("strategyId") Long strategyId, @Param("awardId") Integer awardId);

    void updateStrategyAwardStock(@Param("strategyId") Long strategyId, @Param("awardId") Integer awardId);
}
