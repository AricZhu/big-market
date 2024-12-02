package com.platform.bigmarket.domain.strategy.service;

import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StrategyService implements IStrategyService {

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardList = strategyRepository.queryStrategyAwardEntityList(strategyId);
        BigDecimal totalRate = strategyAwardList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal minRate = strategyAwardList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal rateRange = totalRate.divide(minRate, 0, RoundingMode.CEILING);

        Integer rateRangeInt = rateRange.intValue();
        strategyRepository.setStragetyAwardRange(strategyId, rateRangeInt);

        ArrayList<Integer> rateTableList = new ArrayList<>(rateRangeInt);
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardList) {
            int awardCounts = strategyAwardEntity.getAwardRate().multiply(rateRange).intValue();
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

        strategyRepository.setStrategyAwardRateTable(strategyId, rateTable);
        return true;
    }

    @Override
    public Integer lotteryByStrategyId(Long strategyId) {
        int stragetyAwardRange = strategyRepository.getStragetyAwardRange(strategyId);
        Map<String, Integer> stragetyAwardRateTable = strategyRepository.getStrategyAwardRateTable(strategyId);

        Random random = new Random();
        int randomIndex = random.nextInt(stragetyAwardRange);
        return stragetyAwardRateTable.get(String.valueOf(randomIndex));
    }
}
