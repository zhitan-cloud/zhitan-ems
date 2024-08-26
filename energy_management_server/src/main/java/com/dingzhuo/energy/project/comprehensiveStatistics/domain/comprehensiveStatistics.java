package com.dingzhuo.energy.project.comprehensiveStatistics.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.Quality;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

public  class comprehensiveStatistics implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;

  private String indexId;
  private String indexStorageId;
  @Excel(name = "指标Id")
  private String indexCode;
  private String indexName;
  private Date beginTime;
  private Date endTime;
  private Date dataTime;
  private String timeCode;
  private TimeType timeType;
  @Excel(name = "能耗量")
  private Double value;
  private Quality quality;
  private String unitId;
  private Date createTime;
  private Date updateTime;
  private String remark;
  @Excel(name = "名称")
  private String name;
  private String indexType;
  private String facilityName;
  private String valueScale;
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

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

  public String getIndexType() {
    return indexType;
  }
  public void setIndexType(String indexType) {
    this.indexType = indexType;
  }

  public String getFacilityName() {
    return facilityName;
  }

  public void setFacilityName(String facilityName) {
    this.facilityName = facilityName;
  }

  public String getValueScale() {
    return valueScale;
  }

  public void setValueScale(String valueScale) {
    this.valueScale = valueScale;
  }
}
