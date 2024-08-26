package com.dingzhuo.compute.engine.message.device;

import java.util.Date;

public class DeviceStatus {

  private boolean alarm;
  private Date beginTime;

  public boolean isAlarm() {
    return alarm;
  }

  public void setAlarm(boolean alarm) {
    this.alarm = alarm;
  }

  public Date getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
}
