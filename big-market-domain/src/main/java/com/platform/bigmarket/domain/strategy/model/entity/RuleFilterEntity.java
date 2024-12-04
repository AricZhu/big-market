package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * 抽奖过滤实体
 */
@Builder
@Getter
public class RuleFilterEntity {
    private String userId;
    private Long strategyId;
    private StrategyEntity strategyEntity;
}
