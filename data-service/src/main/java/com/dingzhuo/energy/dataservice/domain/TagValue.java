package com.dingzhuo.energy.dataservice.domain;

import com.dingzhuo.energy.common.utils.DateUtils;

import java.util.Date;

/**
 * @author 智碳未来
 *
 * 测点数据实体类.
 */
public class TagValue {

  /**
   * 测点编码.
   */
  private String tagCode;

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
   * 详细的质量编码.
   */
  private Integer qualityDetail;

  private String showDataTime;

  public String getTagCode() {
    return tagCode;
  }

  public void setTagCode(String tagCode) {
    this.tagCode = tagCode;
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

  public Integer getQualityDetail() {
    return qualityDetail;
  }

  public void setQualityDetail(Integer qualityDetail) {
    this.qualityDetail = qualityDetail;
  }

  public String getShowDataTime() {
    return showDataTime;
  }

  public void setShowDataTime(String format) {
    this.showDataTime = DateUtils.parseDateToStr(format,this.getDataTime());;
  }
  @Override
  public String toString() {
    return "TagValue{" +
        "tagCode='" + tagCode + '\'' +
        ", dataTime=" + dataTime +
        ", value=" + value +
        ", quality=" + quality +
        ", qualityDetail=" + qualityDetail +
        '}';
  }
}
