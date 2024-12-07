package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 抽奖结果实体
 */
@Data
@Builder
public class RaffleAwardEntity {
    /** 抽奖奖品ID - 内部流转使用 */
    private Integer awardId;
    /** 抽奖奖品标题 */
    private String awardTitle;
    /** 抽奖奖品副标题 */
    private String awardSubtitle;
}
