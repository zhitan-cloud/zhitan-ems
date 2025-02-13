package com.zhitan.statisticalAnalysis.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 成本趋势-能源类型
 *
 * @Author: Zhujw
 * @Date: 2023/2/14
 */
@Data
@ApiModel(value = "成本趋势-能源类型", description = "成本趋势-能源类型")
public class CostTrendEnergyTypeItem implements Serializable {

    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private String energyType;

    /**
     * 能源名称
     */
    @ApiModelProperty(value = "能源名称")
    private String energyName;

    /**
     * 累积量
     */
    @ApiModelProperty(value = "累积量")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal accumulation;

    /**
     * 费用
     */
    @ApiModelProperty(value = "费用")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal cost;
}