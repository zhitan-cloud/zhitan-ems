package com.dingzhuo.energy.project.energyShareAnalysis.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 能源数据占比分析 返回vo
 */
@Data
public class EnergyAnalysisVO {


    /**
     * 用能单元名称
     */
    private String energyUnitName;

    /**
     * 本期值
     */
    private BigDecimal value;

    /**
     * 占比值
     */
    private BigDecimal ratio;

    /**
     * 合计值
     */
    private BigDecimal sumValue;
}
