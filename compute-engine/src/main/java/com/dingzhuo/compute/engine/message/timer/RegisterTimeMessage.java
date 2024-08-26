package com.dingzhuo.compute.engine.message.timer;

import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.energy.common.utils.time.TimeType;

public class RegisterTimeMessage extends BaseActorMessage {

  private TimeType timeType;
  private RegisterType registerType;

  public RegisterTimeMessage(String actorId,
      TimeType timeType,
      RegisterType registerType) {
    super(actorId);
    this.timeType = timeType;
    this.registerType = registerType;
  }

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public RegisterType getRegisterType() {
    return registerType;
  }

  public void setRegisterType(RegisterType registerType) {
    this.registerType = registerType;
  }
}
