package com.zhitan.energydata.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EnergyChainYoyVO {

    /**
     * 能源类型编号
     */
    private String energyNo;
    /**
     * 能源类型名称
     */
    private String energyName;

    /**
     * 能源单位
     */
    private String energyUnit;

    /**
     * 合计值
     */
    private BigDecimal samePeriodCount;
    /**
     * 合计值
     */
    private BigDecimal count;

    /**
     * 同比
     */
    private BigDecimal tongbi;

    /**
     * 环比
     */
    private BigDecimal huanbi;

    /** 等价折标系数 */
    private String coefficient;

    /**
     * 转换为吨标煤
     */
    private BigDecimal tonCount;
}

