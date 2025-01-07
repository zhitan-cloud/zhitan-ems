package com.zhitan.carbonemission.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *  碳排放核算
 *
 * @author lsk
 * @date 2024-10-29 17:55
 */
@Getter
@Setter
public class CarbonEmissionDTO {
    @ApiModelProperty(value = "节点id")
    private String nodeId;
    @ApiModelProperty(value = "时间类型：年、月、日")
    private String timeType;
    @ApiModelProperty(value = "查询时间")
    private Date dataTime;
    @ApiModelProperty(value = "排放类型")
    private String emissionType;
}
