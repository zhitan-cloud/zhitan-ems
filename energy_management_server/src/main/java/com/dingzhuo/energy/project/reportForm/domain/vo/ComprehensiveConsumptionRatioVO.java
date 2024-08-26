package com.dingzhuo.energy.project.reportForm.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 综合报表查询能耗占比信息返回 VO
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Data
public class ComprehensiveConsumptionRatioVO {


    /**
     * 能源类型名称
     */
    private String item;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 单位
     */
    private String unit;

    /**
     * 合计值
     */
    private BigDecimal count;

    /**
     * 占比
     */
    private BigDecimal percent;
}