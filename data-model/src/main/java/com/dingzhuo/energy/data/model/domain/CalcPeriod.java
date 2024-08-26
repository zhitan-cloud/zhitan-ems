package com.dingzhuo.energy.data.model.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;

public class CalcPeriod {

  private String name;
  private TimeType timeType;
  private CalcType calcType;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
}
