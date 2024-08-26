package com.dingzhuo.energy.project.home.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页费用占比分析返回 VO
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Data
public class HomeEnergyCostRatioVO {


    /**
     * 能源类型名称
     */
    private String item;

    /**
     * 合计值
     */
    private BigDecimal count;

    /**
     * 占比
     */
    private BigDecimal percent;
}