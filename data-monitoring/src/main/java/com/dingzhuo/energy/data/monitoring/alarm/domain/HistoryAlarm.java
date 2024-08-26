package com.dingzhuo.energy.data.monitoring.alarm.domain;

import com.dingzhuo.energy.dataservice.domain.Quality;
import java.util.Date;

public class HistoryAlarm {

  /**
   * 主键
   */
  private String id;
  /**
   * 指标主键
   */
  private String indexId;
  /**
   * 指标名称
   */
  private String indexName;
  private String itemId;
  /**
   * 报警配置项
   */
  private AlarmItem alarmItem;
  /**
   * 限值
   */
  private double limitingValue;
  /**
   * 报警时的值
   */
  private double alarmValue;
  /**
   * 报警是数据质量
   */
  private Quality alarmValueQuality;
  /**
   * 报警事件
   */
  private Date beginTime;
  /**
   * 报警结束时间
   */
  private Date endTime;
  private String timeCode;
  /**
   * 报警持续时间
   */
  private double duration;
  /**
   * 报警描述
   */
  private String content;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  public AlarmItem getAlarmItem() {
    return alarmItem;
  }

  public void setAlarmItem(AlarmItem alarmItem) {
    this.alarmItem = alarmItem;
  }

  public double getLimitingValue() {
    return limitingValue;
  }

  public void setLimitingValue(double limitingValue) {
    this.limitingValue = limitingValue;
  }

  public double getAlarmValue() {
    return alarmValue;
  }

  public void setAlarmValue(double alarmValue) {
    this.alarmValue = alarmValue;
  }

  public Quality getAlarmValueQuality() {
    return alarmValueQuality;
  }

  public void setAlarmValueQuality(Quality alarmValueQuality) {
    this.alarmValueQuality = alarmValueQuality;
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

  public double getDuration() {
    return duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }
}
