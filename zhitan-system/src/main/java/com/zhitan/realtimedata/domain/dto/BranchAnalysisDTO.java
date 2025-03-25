package com.zhitan.realtimedata.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class BranchAnalysisDTO {


    @ApiModelProperty(value="节点id")
    private String nodeId;
    @ApiModelProperty(value ="时间字符串")
    private String dataTime;
    @ApiModelProperty(value ="时间类型")
    private String timeType;
    @ApiModelProperty(value = "能源类型")
    private String energyType;
}
