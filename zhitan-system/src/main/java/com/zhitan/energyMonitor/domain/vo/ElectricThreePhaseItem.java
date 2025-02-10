package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:59
 */
@Data
public class ElectricThreePhaseItem {
    /**
     * 电表名称
     */
    private String name;
    /**
     * 时间
     */
    private String timeCode;
    /**
     * 统计图显示时间
     */
    private String timeCodeChart;
    /**
     * 最大三相不平衡
     */
    private String max;
    /**
     * 最小三相不平衡
     */
    private String min;
    /**
     * A相电流/电压
     */
    private String valueA;
    /**
     * D相电流/电压
     */
    private String valueB;
    /**
     * C相电流/电压
     */
    private String valueC;
}
