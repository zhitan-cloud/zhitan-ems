package com.dingzhuo.compute.engine.message.save;

import com.dingzhuo.energy.common.utils.time.TimeType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanxinfu
 */
public class SaveMessage {

  private String indexId;
  private String timeCode;
  private TimeType timeType;
  private double value;
  private List<String> postActorIds = new ArrayList<>();

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public List<String> getPostActorIds() {
    return postActorIds;
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
}
