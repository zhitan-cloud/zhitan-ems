package com.zhitan.carbonemission.domain;

import com.zhitan.common.enums.Quality;
import com.zhitan.common.enums.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 周期数据项.
 */
@Data
@ApiModel(value = "dataItem对象-计算碳排放用")
public class CarbonEmission implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;

  @ApiModelProperty(value = "指标id")
  private String indexId;
  @ApiModelProperty(value = "指标库id")
  private String indexStorageId;
  @ApiModelProperty(value = "指标code")
  private String indexCode;
  @ApiModelProperty(value = "指标名称")
  private String indexName;
  @ApiModelProperty(value = "开始时间")
  private Date beginTime;
  @ApiModelProperty(value = "结束时间")
  private Date endTime;
  @ApiModelProperty(value = "日期")
  private Date dataTime;
  @ApiModelProperty(value = "日期字符串")
  private String dataTimeStr;
  @ApiModelProperty(value = "时间类型编码")
  private String timeCode;
  @ApiModelProperty(value = "时间类型")
  private TimeType timeType;
  @ApiModelProperty(value = "值")
  private Double value;
  @ApiModelProperty(value = "质量")
  private Quality quality;
  @ApiModelProperty(value = "")
  private String unitId;
  @ApiModelProperty(value = "创建时间")
  private Date createTime;
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;
  @ApiModelProperty(value = "备注")
  private String remark;
  @ApiModelProperty(value = "能源类型")
  private String energyId;
  @ApiModelProperty(value = "节点名称")
  private String nodeName;
}
