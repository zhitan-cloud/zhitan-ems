package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * description 产品单耗分析
 *
 * @author hmj
 * @date 2024-10-16 17:55
 */
@Getter
@Setter
public class ProductEnergyAnalysisVO {
    

    /**
     * 批次能耗与产量趋势分析
     */
    private List<ProductEnergyAnalysisData> chart;
    
    
    /**
     * 平均批次单耗
     */
    private Double averageEnergy;
    /**
     * 平均批次单耗
     */
    private Double totalEnergy;
    /**
     * 平均批次单耗
     */
    private Double totalProduct;

    /**
     * 同比
     */
    private Double tongbi;
    /**
     * 环比
     */
    private Double huanbi;
}
