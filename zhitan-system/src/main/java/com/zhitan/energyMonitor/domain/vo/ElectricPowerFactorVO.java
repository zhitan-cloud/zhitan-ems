package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:58
 */
@Data
public class ElectricPowerFactorVO {
    /**
     * 记录列表
     */
    private List<ElectricPowerFactorItem> itemList;

    /**
     * 详情实体
     */
    private ElectricPowerFactorDetail detail;
}
