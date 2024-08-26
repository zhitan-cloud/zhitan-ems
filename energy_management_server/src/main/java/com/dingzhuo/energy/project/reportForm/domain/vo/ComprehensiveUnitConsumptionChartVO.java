package com.dingzhuo.energy.project.reportForm.domain.vo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 综合报表获取能耗设备占比信息 返回 vo
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Data
public class ComprehensiveUnitConsumptionChartVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 单位
     */
    private String unit;

    /**
     * 值
     */
    private BigDecimal value;

    /**
     * 合计值
     */
    private BigDecimal total;

}