package com.zhitan.home.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeEnergyStatisticsVO {

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
    private Double count;

    /**
     * 同比
     */
    private Double tongbi;

    /**
     * 环比
     */
    private Double huanbi;

    /** 等价折标系数 */
    private String coefficient;

    /**
     * 转换为吨标煤
     */
    private Double tonCount;
}

