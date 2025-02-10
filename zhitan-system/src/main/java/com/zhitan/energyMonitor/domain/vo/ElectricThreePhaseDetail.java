package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:59
 */
@Data
public class ElectricThreePhaseDetail {
    /**
     * 最大功率因数
     */
    private String max;
    /**
     * 最大功率因数发生时间
     */
    private String maxTime;
    /**
     * 最小功率因数
     */
    private String min;
    /**
     * 最小功率因数发生时间
     */
    private String minTime;

    /**
     * 最大值-A相电流/电压
     */
    private String valueMaxA;
    /**
     * 最大值-B相电流/电压
     */
    private String valueMaxB;
    /**
     * 最大值-C相电流/电压
     */
    private String valueMaxC;

    /**
     * 最小值-A相电流/电压
     */
    private String valueMinA;
    /**
     * 最小值-B相电流/电压
     */
    private String valueMinB;
    /**
     * 最小值-C相电流/电压
     */
    private String valueMinC;

}
