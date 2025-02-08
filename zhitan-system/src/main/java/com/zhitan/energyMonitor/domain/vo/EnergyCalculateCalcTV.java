package com.zhitan.energyMonitor.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 计算点位对应的计算公式
 * @author  zhw
 */
@Data
@ApiModel(value="计算点位对应的计算公式", description="计算点位对应的计算公式")
public class EnergyCalculateCalcTV {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "计算点位主键")
    private String  calculateIndexId;

    @ApiModelProperty(value = "仪表设备主键")
    private String deviceId;

    @ApiModelProperty(value = "仪表名称")
    private String name;

    @ApiModelProperty(value = "计算点位包含的采集点位主键")
    private String collectIndexId;

    @ApiModelProperty(value = "采集点位对应操作符号")
    private String operator;
}
