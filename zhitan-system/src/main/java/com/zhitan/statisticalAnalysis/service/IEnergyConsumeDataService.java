package com.zhitan.statisticalAnalysis.service;

import com.zhitan.statisticalAnalysis.domain.vo.*;

import java.util.List;

/**
 * 能源消耗统计相关查询
 */
public interface IEnergyConsumeDataService {

    /**
     * 成本趋势分析（能源消耗成本）- 获取表格列表数据
     *
     * @param pageNo   页码数
     * @param pageSize 每页数据多少
     * @param timeCode 时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType 时间类型 DAY/MONTH/YEAR
     * @param energyType 能源类型
     * @param modelCode 模型Code
     * @return
     */
    EnergyCostTrendPage listEnergyCostTrend(int pageNo, int pageSize, String timeCode, String timeType,String energyType, String modelCode);

    /**
     * 成本趋势分析（能源消耗成本）
     *
     * @param timeCode 时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType 时间类型 DAY/MONTH/YEAR
     * @param modelCode   模型Code
     * @param energyType 能源类型
     * @return
     */
    List<EnergyConsumeTrendDetailItem> listEnergyCostTrendDetail(String timeCode, String timeType, String modelCode, String energyType);

    /**
     * 同比环比分析
     *
     * @param req            请求参数
     * @param comparisonType 对比类型
     * @return
     */
    public List<EnergyTypeValueContrastedVO> listEnergyTypeYoyInfo(QueryCompareRequest req, String comparisonType);
}
