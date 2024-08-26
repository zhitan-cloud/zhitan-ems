package com.dingzhuo.compute.engine.controller;

import com.dingzhuo.energy.common.utils.time.TimeType;

public class Recalc {

  private String indexId;
  private String dataTime;
  private TimeType timeType;

  public String getDataTime() {
    return dataTime;
  }

  public void setDataTime(String dataTime) {
    this.dataTime = dataTime;
  }

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }
}
