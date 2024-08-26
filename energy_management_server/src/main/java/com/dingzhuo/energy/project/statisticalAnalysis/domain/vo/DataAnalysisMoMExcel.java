package com.dingzhuo.energy.project.statisticalAnalysis.domain.vo;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 数据分析环比 导出Excel
 *
 * @Author: Zhujw
 * @Date: 2023/3/3
 */
@Data
public class DataAnalysisMoMExcel {


    /**
     * 用能单元名称
     */
    @Excel(name = "用能单元")
    private String energyUnitName;

    /**
     * 本期值
     */
    @Excel(name = "本期值")
    private BigDecimal currentValue;

    /**
     * 同期值
     */
    @Excel(name = "同期值")
    private BigDecimal oldValue;

    /**
     * 同比值
     */
    @Excel(name = "同比值")
    private BigDecimal ratio;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String unit;
}
