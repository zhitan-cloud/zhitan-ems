package com.zhitan.energyMonitor.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用能单元与计量器具关系vo
 *
 * @Author: Zhujw
 * @Date: 2023/4/4
 */
@Data
@ApiModel(value = "用能单元与计量器具关系vo-返回vo", description = "用能单元与计量器具关系vo-返回vo")
public class UnitToDeviceRelationVO {

    /**
     * 用能单元id
     */
    @ApiModelProperty(value = "用能单元id")
    private String unitId;

    /**
     * 计量器具id
     */
    @ApiModelProperty(value = "计量器具id")
    private String deviceId;

    /**
     * 计量器具名称
     */
    @ApiModelProperty(value = "计量器具名称")
    private String deviceName;
}