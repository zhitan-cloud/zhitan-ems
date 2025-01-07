package com.zhitan.realtimedata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author zhoubg
 * @date 2024-10-18
 **/
@Data
public class RealTimeHistoryDataVO {

    @ApiModelProperty(value = "时间名称")
    private String name;

    @ApiModelProperty(value = "值")
    private BigDecimal value;
}
