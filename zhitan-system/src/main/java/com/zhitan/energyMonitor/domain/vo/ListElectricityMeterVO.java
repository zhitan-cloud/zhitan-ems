package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:07
 */
@Data
public class ListElectricityMeterVO {
    /**
     * 电表名称
     */
    private String label;
    /**
     * 电表id
     */
    private String code;
}
