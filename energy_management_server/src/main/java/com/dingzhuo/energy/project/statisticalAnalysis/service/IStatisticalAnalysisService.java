package com.dingzhuo.energy.project.statisticalAnalysis.service;

import com.dingzhuo.energy.project.statisticalAnalysis.domain.dto.DataAnalysisMoMDTO;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisMoMVO;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisYoYExcel;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisYoYVO;

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
     *
     * @param dto 查询参数
     * @return
     */
    List<DataAnalysisYoYVO> listElectricDataComparisonYoY(DataAnalysisMoMDTO dto);

    /**
     * 获取电能耗环比数据
     *
     * @param dto 查询参数
     * @return
     */
    List<DataAnalysisMoMVO> listElectricDataComparisonMoM(DataAnalysisMoMDTO dto);

    /**
     * 获取能耗同比数据
     *
     * @param dto 查询参数
     * @return
     */
    List<DataAnalysisYoYVO> listWaterDataComparisonYoY(DataAnalysisMoMDTO dto);

    /**
     * 获取能耗环比数据
     *
     * @param dto 查询参数
     * @return
     */
    List<DataAnalysisMoMVO> listWaterDataComparisonMoM(DataAnalysisMoMDTO dto);
}