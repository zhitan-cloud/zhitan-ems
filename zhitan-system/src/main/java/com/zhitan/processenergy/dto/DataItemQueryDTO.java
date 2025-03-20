package com.zhitan.processenergy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 周期数据项.
 */
@Data
public class DataItemQueryDTO {


  @ApiModelProperty(value = "指标id")
  private String indexId;

  @ApiModelProperty(value = "模型code")
  private String indexCode;

  @ApiModelProperty(value = "时间字符串")
  private String dataTime;

  @ApiModelProperty(value = "时间类型")
  private String timeType;

  @ApiModelProperty(value = "能源类型")
  private String energyType;

}
