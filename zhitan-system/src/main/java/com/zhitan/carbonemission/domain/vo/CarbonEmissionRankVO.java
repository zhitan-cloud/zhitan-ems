package com.zhitan.carbonemission.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarbonEmissionRankVO {


    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "全部碳排放量")
    private Double allValue;
    @ApiModelProperty(value = "水消耗量")
    private Double water;
    @ApiModelProperty(value = "电消耗量")
    private Double steam;
    @ApiModelProperty(value = "用电量")
    private Double electric;
    @ApiModelProperty(value = "同比")
    private String yoyEnery;
    @ApiModelProperty(value = "类型名称")
    private String allEneryType;
    @ApiModelProperty(value = "总用量")
    private Double totalEnery;
}
