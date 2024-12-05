package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BeforeRaffleActionEntity extends RaffleActionEntity {
    private Long strategyId;
    private Integer awardId;
    private Integer weight;
}
