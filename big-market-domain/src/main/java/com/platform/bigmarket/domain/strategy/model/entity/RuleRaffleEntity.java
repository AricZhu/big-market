package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 规则抽奖实体，给实际抽奖使用
 */
@Data
@Builder
public class RuleRaffleEntity<T extends RaffleActionEntity> {
    private String ruleActionCode;
    private String ruleModel;
    private T data;
}
