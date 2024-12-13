package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class StrategyEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 规则模型，rule配置的模型同步到此表，便于使用 */
    private String ruleModels;

    public String[] getRuleModelArray() {
        if (StringUtils.isBlank(this.ruleModels)) {
            return new String[0];
        }
        return ruleModels.split(",");
    }

    @Override
    public String toString() {
        return "StrategyEntity{" +
                "strategyId=" + strategyId +
                ", strategyDesc='" + strategyDesc + '\'' +
                ", ruleModels='" + ruleModels + '\'' +
                '}';
    }
}
