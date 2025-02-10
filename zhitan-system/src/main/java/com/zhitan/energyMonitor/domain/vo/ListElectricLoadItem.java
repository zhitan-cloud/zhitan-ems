package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:59
 */
@Data
public class ListElectricLoadItem {
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
     * 最大负荷
     */
    private String max;
    /**
     * 最小负荷
     */
    private String min;
    /**
     * 平均负荷
     */
    private String avg;
    /**
     * 实时值
     */
    private String value;
    /**
     * 负荷率
     */
    private String rate;
}
