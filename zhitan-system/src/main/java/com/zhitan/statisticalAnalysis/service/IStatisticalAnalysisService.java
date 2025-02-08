package com.zhitan.statisticalAnalysis.service;

import com.zhitan.statisticalAnalysis.domain.dto.DataAnalysisMoMDTO;
import com.zhitan.statisticalAnalysis.domain.vo.DataAnalysisMoMVO;
import com.zhitan.statisticalAnalysis.domain.vo.DataAnalysisYoYVO;

import java.util.List;

/**
 * 统计分析  接口层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
public interface IStatisticalAnalysisService {

    /**
     * 获取电能耗同比数据
     */
    List<DataAnalysisYoYVO> listElectricDataComparisonYoY(DataAnalysisMoMDTO dto);

    /**
     * 获取电能耗环比数据
     */
    List<DataAnalysisMoMVO> listElectricDataComparisonMoM(DataAnalysisMoMDTO dto);

    /**
     * 获取能耗同比数据
     */
    List<DataAnalysisYoYVO> listWaterDataComparisonYoY(DataAnalysisMoMDTO dto);

    /**
     * 获取能耗环比数据
     */
    List<DataAnalysisMoMVO> listWaterDataComparisonMoM(DataAnalysisMoMDTO dto);
}