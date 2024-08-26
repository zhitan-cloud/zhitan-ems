package com.dingzhuo.energy.project.electricity.domain;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.Quality;

import java.io.Serializable;
import java.util.Date;

/**
 * 周期数据项.
 */
public class electricityDataItem implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;

  private String indexId;
  private String indexStorageId;
  private String indexCode;
  private String indexName;
  private Date beginTime;
  private Date endTime;
  private Date dataTime;
  private String timeCode;
  private TimeType timeType;
  private Double value;
  private Quality quality;
  private String unitId;
  private Date createTime;
  private Date updateTime;
  private String remark;
  //时间格式化
  private String formatdate;

  public String getIndexId() {
    return indexId;
  }

  public String getFormatdate() {
    return formatdate;
  }

  public void setFormatdate(String formatdate) {
    this.formatdate = formatdate;
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
