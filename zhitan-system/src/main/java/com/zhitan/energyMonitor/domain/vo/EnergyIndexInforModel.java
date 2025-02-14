package com.zhitan.energyMonitor.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 点位详细信息
 *
 * @Author: Zhujw
 * @Date: 2023/5/26
 */
@Data
public class EnergyIndexInforModel {

  @ApiModelProperty(value = "点位id")
  private String indexId;

  @ApiModelProperty(value = "计量器具id")
  private String meterId;

  @ApiModelProperty(value = "点位名称")
  private String indexName;

  @ApiModelProperty(value = "点位类型(采集、计算)")
  private String indexType;

  @ApiModelProperty(value = "点位code")
  private String indexCode;

  @ApiModelProperty(value = "点位单位")
  private String indexUnit;
}
