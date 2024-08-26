package com.dingzhuo.compute.engine.message.alarm;

import java.util.Date;

public class AlarmStatus {

  private boolean isAlarm;
  private Date beginTime;

  public boolean isAlarm() {
    return isAlarm;
  }

  public void setAlarm(boolean alarm) {
    isAlarm = alarm;
  }

  public Date getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
}
