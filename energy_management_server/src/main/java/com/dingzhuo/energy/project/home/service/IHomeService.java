package com.dingzhuo.energy.project.home.service;

import com.dingzhuo.energy.project.home.domain.vo.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页  接口层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
public interface IHomeService {

    /**
     * 首页获取全厂综合能耗
     *
     * @return
     */
    BigDecimal geEnergyConsumptionSummation(String code);


    /**
     * 首页获取综合能耗占比分析
     *
     * @param code 查询编号
     * @return
     */
    List<HomeEnergyConsumptionRatioVO> getHomeEnergyConsumptionRatio(String code);

    /**
     * 首页获取分类能源统计
     *
     * @param code 查询编号
     * @return
     */
    List<HomeEnergyStatisticsVO> getHomeEnergyStatistic(String code);

    /**
     * 首页获取能源分时监测
     *
     * @param code       查询编号
     * @param energyType 能源类型
     * @return
     */
    HomeEnergyMonitoringHistogramVO getHomeEnergyMonitoring(String code, String energyType);

    /**
     * 首页获取能耗设备占比信息
     *
     * @param code 查询编号
     * @return
     */
    List<HomeEnergyUnitConsumptionChartVO> getHomeEnergyUnitConsumptionRatio(String code);

    /**
     * 首页获取室外温湿度
     *
     * @return 结果
     */
    HomeOutdoorTemperatureVO getHomeOutdoorTemperature();
}