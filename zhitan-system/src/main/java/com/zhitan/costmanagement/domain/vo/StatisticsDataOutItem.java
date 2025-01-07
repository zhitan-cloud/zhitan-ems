package com.zhitan.costmanagement.domain.vo;

import com.zhitan.common.annotation.Excel;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

/**
 * 偏差分析返回对象
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Data
public class StatisticsDataOutItem {

    private BigDecimal electricityNum=BigDecimal.ZERO;

    private BigDecimal electricityFee=BigDecimal.ZERO;

    private BigDecimal powerFactor=BigDecimal.ZERO;

    private BigDecimal electricityNumLast=BigDecimal.ZERO;

    private BigDecimal electricityFeeLast=BigDecimal.ZERO;

    private BigDecimal powerFactorLast=BigDecimal.ZERO;

    private BigDecimal electricityNumYoy=BigDecimal.ZERO;

    private BigDecimal electricityFeeYoy=BigDecimal.ZERO;

    private BigDecimal powerFactorYoy=BigDecimal.ZERO;

    private BigDecimal electricityNumQoq=BigDecimal.ZERO;

    private BigDecimal electricityFeeQoq=BigDecimal.ZERO;

    private BigDecimal powerFactorQoq=BigDecimal.ZERO;
    /** 尖时段用电量 */

    private BigDecimal sharpElectricity=BigDecimal.ZERO;

    /** 峰时段用电量 */

    private BigDecimal peakElectricity=BigDecimal.ZERO;

    /** 平时段用电量 */

    private BigDecimal flatElectricity=BigDecimal.ZERO;

    /** 谷时段用电量 */

    private BigDecimal valleyElectricity=BigDecimal.ZERO;

    /** 尖时段用电量差值 */

    private BigDecimal sharpElectricityDiff=BigDecimal.ZERO;

    /** 峰时段用电量差值 */

    private BigDecimal peakElectricityDiff=BigDecimal.ZERO;

    /** 平时段用电量差值 */

    private BigDecimal flatElectricityDiff=BigDecimal.ZERO;

    /** 谷时段用电量差值 */

    private BigDecimal valleyElectricityDiff=BigDecimal.ZERO;

    private BigDecimal electricityNumDiff=BigDecimal.ZERO;

    private BigDecimal electricityFeeDiff=BigDecimal.ZERO;

    private BigDecimal powerFactorDiff=BigDecimal.ZERO;





}
