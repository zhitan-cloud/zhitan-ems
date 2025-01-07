package com.zhitan.realtimedata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author zhoubg
 * @date 2024-10-16
 **/
@Data
public class RealTimeDataVO {
    @ApiModelProperty(value = "能源类型名称")
    private String energyTypeName;

    @ApiModelProperty(value = "传感器信息集合")
    private List<SensorParamModel> deviceArray;
}
