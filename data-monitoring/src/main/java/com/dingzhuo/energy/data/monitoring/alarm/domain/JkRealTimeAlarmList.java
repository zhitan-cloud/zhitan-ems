package com.dingzhuo.energy.data.monitoring.alarm.domain;


import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class JkRealTimeAlarmList extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 模型节点名称
   * */
  private String modelName;
  /**
   * 指标编号
   * */
  private String code;
  /**
   * 指标名称*/
  private String indexName;
  /**
   * 指标单位类型ID*/
  private String unitId;
  /**
   * 报警级别*/
  private String alarmLevel;
  /**
   * 限值类型
   * */
  private String limitType;
  /**
   * 限值
   * */
  private double limitingValue;
  /**
   * 报警值
   * */
  private double alarmValue;
  /**
   * 指标主键id
   * */
  private String indexId;
  /**
   * 模型节点主键id
   * */
  private String nodeId;
  /**
   * 模型查询层级标志 B本级  ALL包含下级
   * */
  private String eierarchyFlag;
  /**
   * 指标类型 COLLECT采集点  STATISTIC指标
   * */
  private String indexType;

  /**
   * 报警开始时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private Date alarmBeginTime;


  private String id;
  private String alarmValueQuality;
  private String content;

  public Date getAlarmBeginTime() {
    return alarmBeginTime;
  }

  public void setAlarmBeginTime(Date alarmBeginTime) {
    this.alarmBeginTime = alarmBeginTime;
  }

  public String getIndexType() {
    return indexType;
  }

  public void setIndexType(String indexType) {
    this.indexType = indexType;
  }

  public String getEierarchyFlag() {
    return eierarchyFlag;
  }

  public void setEierarchyFlag(String eierarchyFlag) {
    this.eierarchyFlag = eierarchyFlag;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getAlarmLevel() {
    return alarmLevel;
  }

  public void setAlarmLevel(String alarmLevel) {
    this.alarmLevel = alarmLevel;
  }

  public String getLimitType() {
    return limitType;
  }

  public void setLimitType(String limitType) {
    this.limitType = limitType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }


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

//  public AlarmItem getAlarmItem() {
//    return alarmItem;
//  }
//
//  public void setAlarmItem(AlarmItem alarmItem) {
//    this.alarmItem = alarmItem;
//  }

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

  public String getAlarmValueQuality() {
    return alarmValueQuality;
  }

  public void setAlarmValueQuality(String alarmValueQuality) {
    this.alarmValueQuality = alarmValueQuality;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
