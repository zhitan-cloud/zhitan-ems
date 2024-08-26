package com.dingzhuo.energy.data.monitoring.alarm.domain;

import com.dingzhuo.energy.dataservice.domain.Quality;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

public class RealTimeAlarm  {
  private static final long serialVersionUID = 1L;

  /** 主键id */
  private String id;

  /** 指标id */
  @Excel(name = "指标id")
  private String indexId;

  /** 指标名称 */
  @Excel(name = "指标名称")
  private String indexName;

  /** 报警项id */
  @Excel(name = "报警项id")
  private String itemId;

  /** 报警限值 */
  @Excel(name = "报警限值")
  private Double limitingValue;

  /** 报警值 */
  @Excel(name = "报警值")
  private Double alarmValue;

  /** 报警值质量 */
  @Excel(name = "报警值质量")
  private Quality alarmValueQuality;

  private String timeCode;

  /** 报警时间 */
  @Excel(name = "报警时间", width = 30, dateFormat = "yyyy-MM-dd")
  private Date beginTime;

  /** 内容 */
  @Excel(name = "内容")
  private String content;

  private AlarmItem alarmItem;

  public String getAlarmCode() {
    return alarmCode;
  }

  public void setAlarmCode(String alarmCode) {
    this.alarmCode = alarmCode;
  }

  /**
   * 报警编码拼接，indexid:time_slot:limit_type
   */
  private String alarmCode;

  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return id;
  }
  public void setIndexId(String indexId)
  {
    this.indexId = indexId;
  }

  public String getIndexId()
  {
    return indexId;
  }
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
  }

  public String getIndexName()
  {
    return indexName;
  }
  public void setItemId(String itemId)
  {
    this.itemId = itemId;
  }

  public String getItemId()
  {
    return itemId;
  }
  public void setLimitingValue(Double limitingValue)
  {
    this.limitingValue = limitingValue;
  }

  public Double getLimitingValue()
  {
    return limitingValue;
  }
  public void setAlarmValue(Double alarmValue)
  {
    this.alarmValue = alarmValue;
  }

  public Double getAlarmValue()
  {
    return alarmValue;
  }
  public void setAlarmValueQuality(Quality alarmValueQuality)
  {
    this.alarmValueQuality = alarmValueQuality;
  }

  public Quality getAlarmValueQuality()
  {
    return alarmValueQuality;
  }
  public void setBeginTime(Date beginTime)
  {
    this.beginTime = beginTime;
  }

  public Date getBeginTime()
  {
    return beginTime;
  }
  public void setContent(String content)
  {
    this.content = content;
  }

  public String getContent()
  {
    return content;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("indexId", getIndexId())
            .append("indexName", getIndexName())
            .append("itemId", getItemId())
            .append("limitingValue", getLimitingValue())
            .append("alarmValue", getAlarmValue())
            .append("alarmValueQuality", getAlarmValueQuality())
            .append("beginTime", getBeginTime())
            .append("content", getContent())
            .toString();
  }

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }

  public AlarmItem getAlarmItem() {
    return alarmItem;
  }

  public void setAlarmItem(AlarmItem alarmItem) {
    this.alarmItem = alarmItem;
  }
}
