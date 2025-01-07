package com.zhitan.consumptionanalysis.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartData{
    @ApiModelProperty(value = "X轴")
    private String xData;
    @ApiModelProperty(value = "本期值")
    private Double yValue;
    @ApiModelProperty(value = "同期值")
    private Double yCompareValue;
    @ApiModelProperty(value = "比值")
    private Double yQOQ;
}