package com.dingzhuo.energy.project.home.domain.vo;

import java.math.BigDecimal;


/**
 * 能源检测柱状图信息
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
public class HomeEnergyDetectionChart {

    /**
     * 名称
     */
    private String name;

    /**
     * 时间
     */
    private String date;

    /**
     * 值
     */
    private BigDecimal value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}