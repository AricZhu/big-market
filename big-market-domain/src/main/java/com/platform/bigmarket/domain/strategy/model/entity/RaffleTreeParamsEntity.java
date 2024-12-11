package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RaffleTreeParamsEntity {
    private String userId;
    private Long strategyId;
    private Integer awardId;
}
