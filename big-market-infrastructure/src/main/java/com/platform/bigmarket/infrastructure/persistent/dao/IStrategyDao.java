package com.platform.bigmarket.infrastructure.persistent.dao;

import com.platform.bigmarket.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();

    Strategy queryStrategy(Long strategyId);
}
