package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 抽奖接口参数实体
 */
@Data
public class RaffleParamsEntity {
    private String userId;
    private Long strategyId;
}
