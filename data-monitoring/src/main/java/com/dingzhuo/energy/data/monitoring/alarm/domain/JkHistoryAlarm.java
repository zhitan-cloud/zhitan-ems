package com.dingzhuo.energy.data.monitoring.alarm.domain;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class JkHistoryAlarm extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 模型节点名称
   * */
  @Excel(name = "单位名称")
  private String modelName;
  /**
   * 指标编号
   * */
  @Excel(name = "指标编码")
  private String code;
  /**
   * 指标名称*/
  @Excel(name = "指标名称")
  private String indexName;
  /**
   * 指标单位类型ID*/
  @Excel(name = "指标单位")
  private String unitId;
  /**
   * 报警级别*/
  @Excel(name = "报警级别")
  private String alarmLevel;
  /**
   * 限值类型
   * */
  @Excel(name = "限值类型")
  private String limitType;
  /**
   * 限值
   * */
  @Excel(name = "限值")
  private double limitingValue;
  /**
   * 报警值
   * */
  @Excel(name = "报警值")
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

  /** 报警时长 */
  @Excel(name = "持续时长(分钟)")
  private Double duration;

  /**
   * 报警开始时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Excel(name = "报警开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
  private Date alarmBeginTime;

  /**
   * 报警结束时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Excel(name = "报警结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
  private Date alarmEndTime;
  /**
   * SVG文件的ID
   */
  private Integer svgId;
  /** 报警内容 */
  private String content;
  private String id;
  private String alarmValueQuality;

  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public Date getAlarmBeginTime() {
    return alarmBeginTime;
  }

  public void setAlarmBeginTime(Date alarmBeginTime) {
    this.alarmBeginTime = alarmBeginTime;
  }

  public Date getAlarmEndTime() {
    return alarmEndTime;
  }

  public void setAlarmEndTime(Date alarmEndTime) {
    this.alarmEndTime = alarmEndTime;
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

  public Integer getSvgId() {
    return svgId;
  }

  public void setSvgId(Integer svgId) {
    this.svgId = svgId;
  }
}
