package com.zhitan.energydata.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseConsumptionVo {

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
     * 购入成本
     */
    private BigDecimal purchaseSum;

    /**
     * 消耗成本
     */
    private BigDecimal consumptionSum;
}
