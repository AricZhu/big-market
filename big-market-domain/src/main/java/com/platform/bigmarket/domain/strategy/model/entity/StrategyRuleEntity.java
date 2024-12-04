package com.platform.bigmarket.domain.strategy.model.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class StrategyRuleEntity {
    /** 抽奖策略ID */
    private Integer strategyId;
    /** 抽奖奖品ID【规则类型为策略，则不需要奖品ID】 */
    private Integer awardId;
    /** 抽象规则类型；1-策略规则、2-奖品规则 */
    private Integer ruleType;
    /** 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】 */
    private String ruleModel;
    /** 抽奖规则比值 */
    private String ruleValue;
    /** 抽奖规则描述 */
    private String ruleDesc;

    /**
     * 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * {
     *     4000: "102,103,104,105",
     *     5000: "102,103,104,105,106,107",
     *     6000: "102,103,104,105,106,107,108,109"
     * }
     * @return
     */
    public Map<Integer, String> getRuleWeightMap() {
        if (!"rule_weight".equals(ruleModel)) {
            throw new RuntimeException("当前策略不是权重策略: " + ruleModel);
        }

        HashMap<Integer, String> ruleWeightMap = new HashMap<>();
        String[] strArr = ruleValue.split(" ");
        for (String str : strArr) {
            String[] item = str.split(":");
            if (item.length != 2) {
                continue;
            }
            ruleWeightMap.put(Integer.valueOf(item[0]), item[1]);
        }

        return ruleWeightMap;
    }

    public Integer calcWeight(Integer userScore) {
        Map<Integer, String> ruleWeightMap = getRuleWeightMap();
        // 将所有权重按从大到小进行排序
        List<Integer> list = ruleWeightMap.keySet().stream().sorted((a, b) -> b - a).toList();
        for (Integer item : list) {
            if (userScore >= item) {
                return item;
            }
        }

        return -1;
    }
}
