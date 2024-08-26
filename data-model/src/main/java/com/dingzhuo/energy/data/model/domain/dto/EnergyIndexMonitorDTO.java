package com.dingzhuo.energy.data.model.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;


/**
 * 能源点位监测请求 DTO
 *
 * @Author: Zhujw
 * @Date: 2023/3/6
 */
@ApiModel(value = "指标信息")
public class EnergyIndexMonitorDTO {

  /**
   * 点位类型
   */
  @ApiModelProperty(value = "点位类型")
  private String indexType;

  /**
   * 模型id
   */
  @NotBlank(message = "未找到模型信息")
  @ApiModelProperty(value = "模型id")
  private String nodeId;


  public String getIndexType() {
    return indexType;
  }

  public void setIndexType(String indexType) {
    this.indexType = indexType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }
}
