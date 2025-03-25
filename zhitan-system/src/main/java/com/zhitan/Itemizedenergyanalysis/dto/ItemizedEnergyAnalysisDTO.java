package com.zhitan.Itemizedenergyanalysis.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 周期数据项.
 */
@Data
public class ItemizedEnergyAnalysisDTO {

  @ApiModelProperty(value = "时间字符串")
  private String dataTime;

  @ApiModelProperty(value = "时间类型")
  private String timeType;

  @ApiModelProperty(value = "能源类型")
  private String energyType;

  @ApiModelProperty(value = "节点Id")
  private String nodeId;
}
