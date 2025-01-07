package com.zhitan.realtimedata.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @Description 能源点位监测请求 DTO
 * @Author zhoubg
 * @date 2024-10-15
 **/
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

    @ApiModelProperty(value = "能源类型")
    private String energyType;

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

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }
}
