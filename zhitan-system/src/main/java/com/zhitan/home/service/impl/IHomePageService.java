package com.zhitan.home.service.impl;

import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.home.domain.vo.HomeEnergyConsumptionTrendVO;
import com.zhitan.home.domain.vo.HomeEnergyStatisticsVO;
import com.zhitan.home.domain.vo.HomePeakValleyVO;

import java.util.List;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-31 18:07
 */
public interface IHomePageService {
    List<HomeEnergyStatisticsVO> energyConsumptionSummation(String timeType, String modelcode);

    List<HomePeakValleyVO> peakValley(String timeType, String modelcode);

    HomeEnergyConsumptionTrendVO energyConsumptionTrend(String timeType, String modelcode);

    List<RankingEnergyData> energyConsumptionRanking(String modelcode, String timeType);
}
