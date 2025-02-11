package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年05月06日 14:02
 */
@Data
public class ElectricThreePhaseTempModel {

    private Double max;
    private Date maxTime;
    private Double min;
    private Date minTime;
    /**
     * 最大值-A相电流/电压
     */
    private double valueMaxA;
    /**
     * 最大值-B相电流/电压
     */
    private double valueMaxB;
    /**
     * 最大值-C相电流/电压
     */
    private double valueMaxC;

    /**
     * 最小值-A相电流/电压
     */
    private double valueMinA;
    /**
     * 最小值-B相电流/电压
     */
    private double valueMinB;
    /**
     * 最小值-C相电流/电压
     */
    private double valueMinC;
}
