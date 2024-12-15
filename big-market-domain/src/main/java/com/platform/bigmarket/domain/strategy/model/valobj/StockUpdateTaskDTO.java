package com.platform.bigmarket.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class StockUpdateTaskDTO {
    private Long strategyId;
    private Integer awardId;
}
