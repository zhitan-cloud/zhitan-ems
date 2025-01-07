package com.zhitan.consumptionanalysis.service;

import com.zhitan.consumptionanalysis.domain.dto.ConsumptionAnalysisDTO;
import com.zhitan.consumptionanalysis.domain.vo.ConsumptionAnalysisVO;
import com.zhitan.consumptionanalysis.domain.vo.ProductEnergyAnalysisVO;
import com.zhitan.consumptionanalysis.domain.vo.RankingDataVO;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;

import java.util.List;

/**
 * description 能耗对比分析
 *
 * @author hmj
 * @date 2024-10-16 17:54
 */
public interface IConsumptionAnalysisService {
    ConsumptionAnalysisVO getByArea(ConsumptionAnalysisDTO dto);

    List<RankingDataVO> getByDepartment(ConsumptionAnalysisDTO dto);

    ConsumptionAnalysisVO getComprehensiveEnergy(ConsumptionAnalysisDTO dto);

    ConsumptionAnalysisVO getYOY(ConsumptionAnalysisDTO dto);

    List<RankingEnergyData> getEnergyRanking(ConsumptionAnalysisDTO dto);

    ConsumptionAnalysisVO getPlanAndProdCount(ConsumptionAnalysisDTO dto);

    ProductEnergyAnalysisVO getProdEnergy(ConsumptionAnalysisDTO dto);
}
