package com.zhitan.energydata.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FactoryEnergyConsumptionVo {
    /**
     * 本月综合能耗
     */
    private BigDecimal monthConsumption;
    /**
     * 能耗费用
     */
    private BigDecimal monthEnergyAmount;

    /**
     * 碳排放
     */
    private BigDecimal cEmission;

    /**
     * 用能完成指标
     */
    private BigDecimal useEnergyIndex;

    /**
     * 各厂区数据
     *
     */
    private List<FactoryEnergyConsumptionItemVo> itemVoList;



}
