package com.zhitan.costmanagement.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 偏差分析返回对象
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Data
public class StatisticsInfoListOut {

    private String nodeId;

    private BigDecimal electricityNum;

    private BigDecimal electricityFee;


    /** 尖时段用电量 */

    private BigDecimal sharpElectricity;

    /** 峰时段用电量 */

    private BigDecimal peakElectricity;

    /** 平时段用电量 */

    private BigDecimal flatElectricity;

    /** 谷时段用电量 */

    private BigDecimal valleyElectricity;

    private BigDecimal electricityNumYoy;

    private BigDecimal electricityNumQoq;

    private BigDecimal electricityNumRadio;

    List<StatisticsInfoListOut>children;
}
