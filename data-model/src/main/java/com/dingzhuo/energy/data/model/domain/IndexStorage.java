package com.dingzhuo.energy.data.model.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fanxinfu
 */
public class IndexStorage implements Serializable {

  private String id;
  private String indexId;
  private TimeType timeType;
  private CalcType calcType;
  private String calcText;
  private Date createTime;
  private Date updateTime;
  private List<String> paramIndex = new ArrayList<>();

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

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public CalcType getCalcType() {
    return calcType;
  }

  public void setCalcType(CalcType calcType) {
    this.calcType = calcType;
  }

  public String getCalcText() {
    return calcText;
  }

  public void setCalcText(String calcText) {
    this.calcText = calcText;
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

  public List<String> getParamIndex() {
    return paramIndex;
  }
}
