package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyRuleEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StrategyService implements IStrategyAssemble, IStrategyLottery {
    private static final String rateRangePrefix = "range_";
    private static final String rateTablePrefix = "rate_table_";

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleStrategy(Long strategyId) {
        // 全概率装配
        List<StrategyAwardEntity> strategyAwardList = strategyRepository.queryStrategyAwardEntityList(strategyId);
        assembleStrategy(strategyId.toString(), strategyAwardList);

        // 权重装配
        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(strategyId, "rule_weight", null);
        if (strategyRuleEntity == null) {
            return true;
        }

        Map<Integer, String> ruleWeightMap = strategyRuleEntity.getRuleWeightMap();
        ruleWeightMap.forEach((key, value) -> {
            List<StrategyAwardEntity> list = strategyAwardList.stream()
                    .filter(item -> value.contains(item.getAwardId().toString()))
                    .toList();

            // 根据不同积分进行装配奖池
            assembleStrategy(strategyId + "_" + key, list);
        });

        return true;
    }

    @Override
    public void assembleStrategy(String key, List<StrategyAwardEntity> strategyAwardList) {
        BigDecimal totalRate = strategyAwardList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal minRate = strategyAwardList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal scale = new BigDecimal(1).divide(minRate, 0, RoundingMode.CEILING);


        BigDecimal rateRange = scale.multiply(totalRate);

        Integer rateRangeInt = rateRange.intValue();
        strategyRepository.setStragetyAwardRange(rateRangePrefix + key, rateRangeInt);

        ArrayList<Integer> rateTableList = new ArrayList<>(rateRangeInt);
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardList) {
            int awardCounts = strategyAwardEntity.getAwardRate().multiply(scale).intValue();
            Integer awardId = strategyAwardEntity.getAwardId();
            for(int i = 0; i < awardCounts; i++) {
                rateTableList.add(awardId);
            }
        }
        Collections.shuffle(rateTableList);

        // 这里保存 <String, Integer>，方便 redis 序列化存储，否则使用 Hash 存储会导致内存爆炸(需要将每个 key 作为一个对象，很耗内存)
        HashMap<String, Integer> rateTable = new HashMap<>();
        for (int i = 0; i < rateTableList.size(); i++) {
            rateTable.put(String.valueOf(i), rateTableList.get(i));
        }

        strategyRepository.setStrategyAwardRateTable(rateTablePrefix + key, rateTable);
    }

    @Override
    public Integer doLottery(Long strategyId) {
        int stragetyAwardRange = strategyRepository.getStragetyAwardRange(rateRangePrefix + strategyId);
        Map<String, Integer> stragetyAwardRateTable = strategyRepository.getStrategyAwardRateTable(rateTablePrefix + strategyId);

        Random random = new Random();
        int randomIndex = random.nextInt(stragetyAwardRange);
        return stragetyAwardRateTable.get(String.valueOf(randomIndex));
    }

    @Override
    public Integer doLotteryByWeight(Long strategyId, Integer weight) {
        int stragetyAwardRange = strategyRepository.getStragetyAwardRange(rateRangePrefix + strategyId + "_" + weight);
        Map<String, Integer> stragetyAwardRateTable = strategyRepository.getStrategyAwardRateTable(rateTablePrefix + strategyId + "_" + weight);

        Random random = new Random();
        int randomIndex = random.nextInt(stragetyAwardRange);
        return stragetyAwardRateTable.get(String.valueOf(randomIndex));
    }
}
