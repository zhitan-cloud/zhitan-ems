package com.zhitan.energydata.service;


import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.energydata.vo.EnergyChainYoyVO;
import com.zhitan.energydata.vo.FactoryEnergyConsumptionVo;
import com.zhitan.energydata.vo.PurchaseConsumptionVo;
import com.zhitan.peakvalley.domain.dto.PeakValleyDTO;
import com.zhitan.peakvalley.domain.vo.peakvalley.PeakValleyDayVO;
import com.zhitan.peakvalley.domain.vo.peakvalley.PeakValleyHourVO;

import java.util.List;

public interface IEnergyDataStatisticService {
  PeakValleyDayVO segmentAnalysisMonth(PeakValleyDTO dto);

  List<RankingEnergyData> energyConsumptionRanking(String modelCode, String timeType);

  FactoryEnergyConsumptionVo factoryEnergyConsumption(String timeType, String modelCode);

  List<PurchaseConsumptionVo> purchaseConsumption(String modelCode, String timeType );

  List<PurchaseConsumptionVo> costProp(String modelCode, String timeType);

  List<EnergyChainYoyVO> energyConsumptionSummation(String timeType, String modelCode);

  PeakValleyDayVO segmentAnalysisDay(PeakValleyDTO dto);

  PeakValleyHourVO segmentAnalysisHour(PeakValleyDTO dto);
}
