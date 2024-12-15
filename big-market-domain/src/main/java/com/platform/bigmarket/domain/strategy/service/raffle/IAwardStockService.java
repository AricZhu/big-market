package com.platform.bigmarket.domain.strategy.service.raffle;

import com.platform.bigmarket.domain.strategy.model.valobj.StockUpdateTaskDTO;

public interface IAwardStockService {
    StockUpdateTaskDTO getStockUpdateTask();
    void updateAwardStock(StockUpdateTaskDTO stockUpdateTaskDTO);
}
