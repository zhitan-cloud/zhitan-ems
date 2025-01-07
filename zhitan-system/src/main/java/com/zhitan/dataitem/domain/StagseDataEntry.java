package com.zhitan.dataitem.domain;

import com.zhitan.common.enums.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * stagseDataEntry对象 data_item
 *
 * @author sys
 * @date 2020-03-25
 */
@Data
@ApiModel(value = "阶段录入实体")
public class StagseDataEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "节点id")
    private String nodeId;
    @ApiModelProperty(value = "时间类型")
    private TimeType timeType;
    @ApiModelProperty(value = "指标id")
    private String indexId;
    @ApiModelProperty(value = "指标编码")
    private String indexCode;
    @ApiModelProperty(value = "计算方式")
    private String calcType;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "单位")
    private String unitId;
    @ApiModelProperty(value = "值")
    private Double value;
    @ApiModelProperty(value = "时间编码")
    private String timeCode;
    @ApiModelProperty(value = "日期")
    private Date dataTime;
    @ApiModelProperty(value = "日期字符串")
    private String dataTimeStr;
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
