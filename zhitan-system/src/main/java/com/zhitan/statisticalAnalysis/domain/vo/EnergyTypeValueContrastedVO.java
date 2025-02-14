package com.zhitan.statisticalAnalysis.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 各能源类型值同比、环比VO
 *
 * @Author: Zhujw
 * @Date: 2023/2/8
 */
@Data
public class EnergyTypeValueContrastedVO {

    /**
     * 用能单元名称
     */
    @ApiModelProperty(value = "用能单元名称")
    private String energyUnitName;

    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private String energyType;

    /**
     * 本期时间
     */
    @ApiModelProperty(value = "本期时间")
    private String currentTime;

    /**
     * 本期值
     */
    @ApiModelProperty(value = "本期值")
    private BigDecimal currentValue;

    /**
     * 对比时间
     */
    @ApiModelProperty(value = "对比时间")
    private String compareTime;

    /**
     * 对比值
     */
    @ApiModelProperty(value = "对比值")
    private BigDecimal contrastValues;

    /**
     * 同比值
     * 同比增长率=（本期值-去年同期值）/去年同期值×100%
     * 环比值
     * 环比增长率=（本期值-上期值）/上期值×100%
     */
    @ApiModelProperty(value = "同比增长率/环比增长率")
    private BigDecimal ratio;

}