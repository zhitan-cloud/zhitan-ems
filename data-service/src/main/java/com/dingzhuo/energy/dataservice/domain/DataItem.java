package com.dingzhuo.energy.dataservice.domain;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 周期数据项.
 */
@ApiModel(value = "dataItem对象")
public class DataItem implements Serializable {
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

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getIndexStorageId() {
    return indexStorageId;
  }

  public void setIndexStorageId(String indexStorageId) {
    this.indexStorageId = indexStorageId;
  }

  public String getIndexCode() {
    return indexCode;
  }

  public void setIndexCode(String indexCode) {
    this.indexCode = indexCode;
  }

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  public Date getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getDataTime() {
    return dataTime;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Quality getQuality() {
    return quality;
  }

  public void setQuality(Quality quality) {
    this.quality = quality;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
