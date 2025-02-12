package com.zhitan.statisticalAnalysis.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月15日 10:07
 */
@Data
public class EnergyCostTrendPage {
    /**
     * 数据列表
     */
    private List<EnergyCostTrendItem> itemList;
    /**
     * 记录总数量
     */
    private long total;
}
