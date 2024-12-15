package com.platform.bigmarket.domain.strategy.service.raffle;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.domain.strategy.model.common.RuleModel;
import com.platform.bigmarket.domain.strategy.model.entity.*;
import com.platform.bigmarket.domain.strategy.model.valobj.StockUpdateTaskDTO;
import com.platform.bigmarket.domain.strategy.repository.IStrategyRepository;
import com.platform.bigmarket.domain.strategy.service.IStrategyLottery;
import com.platform.bigmarket.domain.strategy.service.chain.factory.RuleFilterChainFactory;
import com.platform.bigmarket.domain.strategy.service.tree.factory.DefaultTreeLogicFactory;
import com.platform.bigmarket.types.common.Constants;
import com.platform.bigmarket.types.common.ExceptionCode;
import com.platform.bigmarket.types.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽奖抽象类，定义抽奖的流程
 */
@Slf4j
public abstract class AbstractRaffleService implements IRaffleService, IAwardStockService {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Autowired
    private IStrategyLottery strategyLottery;

    @Autowired
    private RuleFilterChainFactory ruleFilterChainFactory;


    @Override
    public RaffleAwardEntity performRaffle(RaffleParamsEntity raffleParams) {
        if (StringUtils.isBlank(raffleParams.getUserId()) || raffleParams.getStrategyId() == null) {
            throw new BizException(ExceptionCode.ILLEGAL_PARAMS);
        }

        // 抽奖前-责任链过滤
        RuleFilterChainFactory.RuleFilterChainAwardEntity ruleFilterChainAwardEntity = this.doLogicChainFilter(raffleParams);
        // 非默认抽奖时，直接返回
        if (!RuleModel.DEFAULT.getCode().equals(ruleFilterChainAwardEntity.getRuleModel().getCode())) {
            return RaffleAwardEntity.builder()
                    .awardId(ruleFilterChainAwardEntity.getAwardId())
                    .build();
        }
        log.info("责任链过滤结果: {}", JSON.toJSONString(ruleFilterChainAwardEntity));

        // 抽奖中-规则树过滤
        DefaultTreeLogicFactory.RuleFilterTreeAwardEntity ruleFilterTreeAwardEntity = this.doLogicTreeFilter(new RaffleTreeParamsEntity(raffleParams.getUserId(), raffleParams.getStrategyId(), ruleFilterChainAwardEntity.getAwardId()));

        log.info("规则树过滤结果: {}", JSON.toJSONString(ruleFilterTreeAwardEntity));

        return RaffleAwardEntity.builder()
                .awardId(ruleFilterTreeAwardEntity.getAwardId())
                .awardValue(ruleFilterTreeAwardEntity.getAwardValue())
                .build();
    }

    protected abstract RuleFilterChainFactory.RuleFilterChainAwardEntity doLogicChainFilter(RaffleParamsEntity raffleParams);
    protected abstract DefaultTreeLogicFactory.RuleFilterTreeAwardEntity doLogicTreeFilter(RaffleTreeParamsEntity raffleTreeParamsEntity);

    @Override
    public StockUpdateTaskDTO getStockUpdateTask() {
        String key = Constants.STOCK_UPDATE_TASK_PREFIX;
        return strategyRepository.getStockUpdateTask(key);
    }

    @Override
    public void updateAwardStock(StockUpdateTaskDTO stockUpdateTaskDTO) {
        strategyRepository.updateAwardStock(stockUpdateTaskDTO);
    }
}
