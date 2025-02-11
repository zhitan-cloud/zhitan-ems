package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:59
 */
@Data
public class ElectricPowerFactorItem {
    /**
     * 时间
     */
    private String timeCode;
    /**
     * 实时值
     */
    private String value;
}
