package com.dingzhuo.energy.project.energyStatistics.domain;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

import java.io.Serializable;
import java.util.Date;


public class dataTimeSVG implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;
  @Excel(name = "指标id")
  private String indexId;
  @Excel(name = "指标编码")
  private String indexCode;
  @Excel(name = "指标名称")
  private String indexName;
  @Excel(name = "开始时间")
  private Date beginTime;
  @Excel(name = "结束时间")
  private Date endTime;
  @Excel(name = "时间")
  private Date dataTime;
  @Excel(name = "timeCode")
  private String timeCode;
  @Excel(name = "时间类型")
  private TimeType timeType;
  @Excel(name = "指标值")
  private Double value;
  @Excel(name = "单位")
  private String unitId;
  @Excel(name = "备注")
  private String remark;
  @Excel(name = "计量器具id")
  private String meterId;
  //模型节点主键id
  private String nodeId;
  private String indexType;
  //时间格式化
  private String formatdate;
  public String getFormatdate() {
    return formatdate;
  }
  public void setFormatdate(String formatdate) {
    this.formatdate = DateUtils.parseDateToStr(formatdate,this.dataTime);
  }
  /**
   * SVG文件的ID
   */
  private Integer svgId;

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
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

  public String getMeterId() {
    return meterId;
  }

  public void setMeterId(String meterId) { this.meterId = meterId; }

  public Integer getSvgId() {
    return svgId;
  }

  public void setSvgId(Integer svgId) {
    this.svgId = svgId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getIndexType() {
    return indexType;
  }

  public void setIndexType(String indexType) {
    this.indexType = indexType;
  }

  public String getUnitId() {
    return unitId;
  }
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }
}
