package com.dingzhuo.energy.dataservice.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.StringUtils;
import com.dingzhuo.energy.common.utils.DateUtils;
import java.util.Date;

public class StatisticResult extends BaseEntity {
  /**
   * 指标主键
   */
  @Excel(name = "指标主键")
  private String indexId;
  /**
   * 指标编码
   */
  @Excel(name = "指标编码")
  private String indexCode;
  /**
   * 指标名称
   */
  @Excel(name = "指标名称")
  private String indexName;
  /**
   * 单位主键
   */
  @Excel(name = "单位主键")
  private String unitId;
  /**
   * 数据时间
   */
  @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
  private Date dataTime;
  /**
   * 本期值
   */
  @Excel(name = "本期值")
  private double currentValue;
  /**
   * 上期值
   */
  @Excel(name = "上期值")
  private double previousValue;
  /**
   * 同期值
   */
  @Excel(name = "同期值")
  private double lastYearValue;
  /**
   * 最大值
   */
  @Excel(name = "最大值")
  private double maxValue;
  /**
   * 最小值
   */
  @Excel(name = "最小值")
  private double minValue;
  /**
   * 平均值
   */
  @Excel(name = "平均值")
  private double avgValue;
  //时间
  private String formatdate;

  public String getFormatdate() {
    return formatdate;
  }

  public void setFormatdate(String formatdate) {
    this.formatdate = DateUtils.parseDateToStr(formatdate,this.dataTime);
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

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public Date getDataTime() {
    return dataTime;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }

  public double getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(double currentValue) {
    this.currentValue = currentValue;
  }

  public double getPreviousValue() {
    return previousValue;
  }

  public void setPreviousValue(double previousValue) {
    this.previousValue = previousValue;
  }

  public double getLastYearValue() {
    return lastYearValue;
  }

  public void setLastYearValue(double lastYearValue) {
    this.lastYearValue = lastYearValue;
  }

  public double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * 同比.
   */
  public double getYoy() {
    if (lastYearValue != 0) {
      return (currentValue - lastYearValue) / lastYearValue * 100;
    }
    return 0;
  }

  /**
   * 环比.
   * @return
   */
  public double getQoq() {
    if (previousValue != 0) {
      return (currentValue - previousValue) / previousValue * 100;
    }
    return 0;
  }

  public boolean isEmpty() {
    return StringUtils.isEmpty(indexId);
  }

  public double getMinValue() {
    return minValue;
  }

  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }

  public double getAvgValue() {
    return avgValue;
  }

  public void setAvgValue(double avgValue) {
    this.avgValue = avgValue;
  }

}
