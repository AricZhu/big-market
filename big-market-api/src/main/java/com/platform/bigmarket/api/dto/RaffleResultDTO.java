package com.platform.bigmarket.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaffleResultDTO {
    // 奖品ID
    private Integer awardId;
    // 排序编号【策略奖品配置的奖品顺序编号】
    private Integer awardIndex;
}
