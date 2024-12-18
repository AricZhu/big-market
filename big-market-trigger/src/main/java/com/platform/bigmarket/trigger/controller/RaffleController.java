package com.platform.bigmarket.trigger.controller;


import com.platform.bigmarket.api.IRaffleServiceAPI;
import com.platform.bigmarket.api.dto.AwardItemDTO;
import com.platform.bigmarket.api.dto.AwardListRequestDTO;
import com.platform.bigmarket.api.dto.RaffleRequestDTO;
import com.platform.bigmarket.api.dto.RaffleResultDTO;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleAwardEntity;
import com.platform.bigmarket.domain.strategy.model.entity.RaffleParamsEntity;
import com.platform.bigmarket.domain.strategy.model.entity.StrategyAwardEntity;
import com.platform.bigmarket.domain.strategy.service.raffle.IRaffleAwardService;
import com.platform.bigmarket.domain.strategy.service.raffle.IRaffleService;
import com.platform.bigmarket.domain.strategy.service.strategy.IStrategyAssemble;
import com.platform.bigmarket.types.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class RaffleController implements IRaffleServiceAPI {

    @Autowired
    private IStrategyAssemble strategyAssemble;

    @Autowired
    private IRaffleService raffleService;

    @Autowired
    private IRaffleAwardService raffleAwardService;


    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyAssemble(@RequestParam("strategyId") Long strategyId) {
        try {
            boolean status = strategyAssemble.assembleStrategy(strategyId);
            if (status) {
                return Response.success(status);
            }
            return Response.fail(status, "装配失败");
        } catch (Exception e) {
            return Response.fail(false, e.getMessage());
        }

    }

    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<AwardItemDTO>> queryAwardList(@RequestBody AwardListRequestDTO awardListRequestDTO) {
        List<StrategyAwardEntity> strategyAwardEntities = raffleAwardService.queryAwardList(awardListRequestDTO.getStrategyId());
        ArrayList<AwardItemDTO> awardItemList = new ArrayList<>();

        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            AwardItemDTO awardItemDTO = new AwardItemDTO();
            BeanUtils.copyProperties(strategyAwardEntity, awardItemDTO);
            awardItemList.add(awardItemDTO);
        }

        return Response.success(awardItemList);
    }

    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleResultDTO> doRaffle(@RequestBody RaffleRequestDTO raffleRequestDTO) {
        RaffleParamsEntity raffleParamsEntity = new RaffleParamsEntity();
        raffleParamsEntity.setStrategyId(raffleRequestDTO.getStrategyId());
        raffleParamsEntity.setUserId("system");
        RaffleAwardEntity raffleAwardEntity = raffleService.performRaffle(raffleParamsEntity);

        RaffleResultDTO raffleResultDTO = RaffleResultDTO.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .awardIndex(raffleAwardEntity.getSort())
                .build();

        return Response.success(raffleResultDTO);
    }
}
