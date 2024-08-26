package com.dingzhuo.energy.project.reportForm.service;

import com.dingzhuo.energy.project.reportForm.domain.dto.ComprehensiveReportsDTO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveConsumptionRatioVO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveReportsItem;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveUnitConsumptionChartVO;

import java.util.List;

/**
 * 综合报表 接口层
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
public interface IComprehensiveReportsService {

    /**
     * 综合报表查询列表信息
     *
     * @param dto 请求参数
     * @return
     */
    List<ComprehensiveReportsItem> listComprehensiveReport(ComprehensiveReportsDTO dto);

    /**
     * 综合报表查询能耗占比信息
     *
     * @param dto 请求参数
     * @return
     */
    List<ComprehensiveConsumptionRatioVO> getEnergyConsumptionRatio(ComprehensiveReportsDTO dto);

    /**
     * 综合报表获取能耗设备占比信息
     *
     * @param dto 请求参数
     * @return
     */
    List<ComprehensiveUnitConsumptionChartVO> getEnergyUnitConsumptionRatio(ComprehensiveReportsDTO dto);

    /**
     * 综合报表查询用能单元柱状图信息
     *
     * @param dto 请求参数
     * @return
     */
    List<ComprehensiveReportsItem> listEnergyUnitComprehensiveReport(ComprehensiveReportsDTO dto);
}
