package com.platform.bigmarket.domain.strategy.service.raffle;

import com.platform.bigmarket.domain.strategy.model.entity.RaffleAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleParamsEntity;

public interface IRaffleService {
    RaffleAwardEntity performRaffle(RaffleParamsEntity raffleParams);
}
