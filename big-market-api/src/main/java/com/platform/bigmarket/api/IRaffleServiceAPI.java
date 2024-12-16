package com.platform.bigmarket.api;

import com.platform.bigmarket.api.dto.AwardItemDTO;
import com.platform.bigmarket.api.dto.AwardListRequestDTO;
import com.platform.bigmarket.api.dto.RaffleRequestDTO;
import com.platform.bigmarket.api.dto.RaffleResultDTO;
import com.platform.bigmarket.types.common.Response;

import java.util.List;

/**
 * 对外提供的抽奖 API 接口
 */
public interface IRaffleServiceAPI {
    /**
     * 策略装配
     * @param strategyId
     * @return
     */
    Response<Boolean> strategyAssemble(Long strategyId);

    /**
     * 奖品列表
     */
    Response<List<AwardItemDTO>> queryAwardList(AwardListRequestDTO awardListRequestDTO);

    /**
     * 抽奖
     */
    Response<RaffleResultDTO> doRaffle(RaffleRequestDTO raffleRequestDTO);
}
