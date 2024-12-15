package com.platform.bigmarket.trigger.job;

import com.platform.bigmarket.domain.strategy.model.valobj.StockUpdateTaskDTO;
import com.platform.bigmarket.domain.strategy.service.raffle.IAwardStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AwardStockUpdateTask {
    @Autowired
    private IAwardStockService awardStockService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void scheduleFixedRateTask() {
        StockUpdateTaskDTO stockUpdateTask = awardStockService.getStockUpdateTask();
        if (stockUpdateTask == null) {
            log.info("定时任务，更新队列为空");

            return;
        }
        log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}", stockUpdateTask.getStrategyId(), stockUpdateTask.getAwardId());

        awardStockService.updateAwardStock(stockUpdateTask);
    }
}
