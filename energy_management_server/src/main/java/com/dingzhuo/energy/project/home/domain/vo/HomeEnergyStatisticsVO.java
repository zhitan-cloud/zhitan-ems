package com.dingzhuo.energy.project.home.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页获取分类能源统计返回 VO
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Data
public class HomeEnergyStatisticsVO {


    /**
     * 能源类型名称
     */
    private String item;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 合计值
     */
    private String count;
}