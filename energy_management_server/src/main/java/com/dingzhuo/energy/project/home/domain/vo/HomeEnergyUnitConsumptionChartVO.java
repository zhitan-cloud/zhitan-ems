package com.dingzhuo.energy.project.home.domain.vo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 首页获取能耗设备占比 返回 vo
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Data
public class HomeEnergyUnitConsumptionChartVO {

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