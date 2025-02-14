package com.zhitan.statisticalAnalysis.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 能流图查询
 */
@Data
public class FlowChartsItemVO {

    /**
     * 源头
     */
    @ApiModelProperty(value = "源头")
    private String source;

    /**
     * 目标
     */
    @ApiModelProperty(value = "目标")
    private String target;

    /**
     * 值
     */
    @NotBlank(message = "值")
    @ApiModelProperty(value = "值")
    private BigDecimal value;

}
