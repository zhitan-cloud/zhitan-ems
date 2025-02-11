package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:59
 */
@Data
public class ListElectricLoadDetail {
    /**
     * 最大负荷
     */
    private String max;
    /**
     * 最大负荷发生时间
     */
    private String maxTime;
    /**
     * 最小负荷
     */
    private String min;
    /**
     * 最小负荷发生时间
     */
    private String minTime;
    /**
     * 平均负荷
     */
    private String avg;
    /**
     * 负荷率 = 平均负荷 / 最大负荷
     */
    private String rate;
}
