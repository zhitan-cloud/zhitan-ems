package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月24日 16:58
 */
@Data
public class ListElectricLoadVO {
    /**
     * 记录列表
     */
    private List<ListElectricLoadItem> itemList;

    /**
     * 详情实体
     */
    private ListElectricLoadDetail detail;
}
