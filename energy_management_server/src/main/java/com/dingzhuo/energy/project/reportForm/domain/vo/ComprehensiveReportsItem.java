package com.dingzhuo.energy.project.reportForm.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 综合报表返回详细信息
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
@Data
public class ComprehensiveReportsItem {

    /**
     * 用能单元
     */
    private String energyUnit;

    /**
     * 年份1
     */
    private BigDecimal value;

    /**
     * 年份2
     */
    private BigDecimal value2;

    /**
     * 年份3
     */
    private BigDecimal value3;

    /**
     * 年份4
     */
    private BigDecimal value4;
}
