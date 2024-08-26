package com.dingzhuo.energy.project.home.domain.vo;

import java.util.List;


/**
 * 能源监测柱状图数据 VO
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
//@Data
public class HomeEnergyMonitoringHistogramVO {
    private static final long serialVersionUID = 1L;

    /**
     * 单位信息
     */
    private String unit;

    /**
     * 图形数据
     */
    private List<HomeEnergyDetectionChart> chartData;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<HomeEnergyDetectionChart> getChartData() {
        return chartData;
    }

    public void setChartData(List<HomeEnergyDetectionChart> chartData) {
        this.chartData = chartData;
    }
}