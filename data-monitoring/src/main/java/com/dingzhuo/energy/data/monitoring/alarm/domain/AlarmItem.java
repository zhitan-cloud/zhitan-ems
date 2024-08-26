package com.dingzhuo.energy.data.monitoring.alarm.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 预报警设置对象 warn_set
 *
 * @author sys
 * @date 2020-03-02
 */
public class AlarmItem extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  private String id;

  /**
   * 指标的主键id
   */
  @Excel(name = "指标的主键id")
  private String dwid;

  /**
   * 指标编码
   */
  private String indexCode;

  /**
   * 启停状态 1启动2停止
   */
  @Excel(name = "启停状态 1启动2停止")
  private String startStop;

  /**
   * 时段
   */
  @Excel(name = "时段")
  private String timeSlot;

  /**
   * 限   1上限2下限
   */
  @Excel(name = "限   1上限2下限")
  private String limitType;

  /**
   * 限值
   */
  @Excel(name = "限值")
  private String limitVal;

  /**
   * 报警级别
   */
  @Excel(name = "报警级别")
  private String alarmLevel;

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

  /**
   * nodeid
   */
  @Excel(name = "节点ID")
  private String nodeId;

  private String calcText;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setDwid(String dwid) {
    this.dwid = dwid;
  }

  public String getDwid() {
    return dwid;
  }

  public void setStartStop(String startStop) {
    this.startStop = startStop;
  }

  public String getStartStop() {
    return startStop;
  }

  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  public String getTimeSlot() {
    return timeSlot;
  }

  public void setLimitType(String limitType) {
    this.limitType = limitType;
  }

  public String getLimitType() {
    return limitType;
  }

  public void setLimitVal(String limitVal) {
    this.limitVal = limitVal;
  }

  public String getLimitVal() {
    return limitVal;
  }

  public void setAlarmLevel(String alarmLevel) {
    this.alarmLevel = alarmLevel;
  }

  public String getAlarmLevel() {
    return alarmLevel;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getNodeId() {
    return nodeId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", getId())
        .append("createTime", getCreateTime())
        .append("createBy", getCreateBy())
        .append("updateTime", getUpdateTime())
        .append("updateBy", getUpdateBy())
        .append("dwid", getDwid())
        .append("startStop", getStartStop())
        .append("timeSlot", getTimeSlot())
        .append("limitType", getLimitType())
        .append("limitVal", getLimitVal())
        .append("alarmLevel", getAlarmLevel())
        .append("nodeId", getNodeId())
        .toString();
  }

    public String getCalcText() {
        return calcText;
    }

    public void setCalcText(String calcText) {
        this.calcText = calcText;
    }

  public String getIndexCode() {
    return indexCode;
  }

  public void setIndexCode(String indexCode) {
    this.indexCode = indexCode;
  }
}
