package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * description 能耗对比分析
 *
 * @author hmj
 * @date 2024-10-16 17:55
 */
@Getter
@Setter
public class ConsumptionAnalysisVO {
    
    private List<ConsumptionAnalysisData> dataList;
    
    private List<ChartData> chartDataList;
    
    private List<EnergyProportion> energyProportion;
    
    private ConsumptionAnalysisData tongbi;
    
    private ConsumptionAnalysisData huanbi;
    
    private Double planCount;
    
    private Double prodCount;
}
