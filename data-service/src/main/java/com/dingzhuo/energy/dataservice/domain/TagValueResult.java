package com.dingzhuo.energy.dataservice.domain;

import java.util.Date;

public class TagValueResult {

  /**
   * 测点编码.
   */
  private String tagCode;

  private String tagName;
  private String unitId;

  /**
   * 测点数据时间.
   */
  private Date dataTime;

  /**
   * 测点值.
   */
  private Double value;

  /**
   * 测点数据质量.
   */
  private Quality quality;
  /**
   * 计量器具名称
   */
  private String meteName;

  public String getTagCode() {
    return tagCode;
  }

  public void setTagCode(String tagCode) {
    this.tagCode = tagCode;
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public Date getDataTime() {
    return dataTime;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
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

  public String getMeteName() {
    return meteName;
  }

  public void setMeteName(String meteName) {
    this.meteName = meteName;
  }
}
