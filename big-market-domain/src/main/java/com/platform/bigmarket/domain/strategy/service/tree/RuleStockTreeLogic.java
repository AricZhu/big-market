package com.platform.bigmarket.domain.strategy.service.tree;

import com.platform.bigmarket.domain.strategy.model.common.RuleAction;
import com.platform.bigmarket.domain.strategy.model.valobj.StockUpdateTaskDTO;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.strategy.IStrategyAssemble;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import com.platform.bigmarket.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_stock")
public class RuleStockTreeLogic implements ITreeLogic {
    @Autowired
    private IStrategyAssemble strategyAssemble;

    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeLogicFactory.TreeRuleActionAward logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-库存过滤 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

        Boolean status = strategyAssemble.subtractionAwardStock(strategyId, awardId);

        // 库存扣减成功 - 发放奖品
        if (status) {
            log.info("规则过滤-库存过滤-成功 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
            strategyRepository.addStockUpdateTask(Constants.STOCK_UPDATE_TASK_PREFIX, StockUpdateTaskDTO.builder()
                            .strategyId(strategyId)
                            .awardId(awardId)
                    .build());

            return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                    .ruleAction(RuleAction.TAKE_OVER)
                    .awardDataEntity(DefaultTreeLogicFactory.RuleFilterTreeAwardEntity.builder()
                            .awardId(awardId)
                            .awardValue(ruleValue)
                            .build())
                    .build();
        }

        // 扣减失败 - 兜底奖励
        log.info("规则过滤-库存过滤-失败 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultTreeLogicFactory.TreeRuleActionAward.builder()
                .ruleAction(RuleAction.ALLOW)
                .build();
    }
}
