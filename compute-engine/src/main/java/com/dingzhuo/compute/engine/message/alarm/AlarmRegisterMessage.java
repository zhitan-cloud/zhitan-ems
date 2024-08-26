package com.dingzhuo.compute.engine.message.alarm;

import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;

/**
 * @author fanxinfu
 */
public class AlarmRegisterMessage extends BaseActorMessage {

  private RegisterType registerType;
  private AlarmType alarmType;
  private AlarmItem alarmItem;

  public AlarmRegisterMessage(String actorId, AlarmType alarmType,
      RegisterType registerType,
      AlarmItem alarmItem) {
    super(actorId);
    this.alarmType = alarmType;
    this.registerType = registerType;
    this.alarmItem = alarmItem;
  }

  public RegisterType getRegisterType() {
    return registerType;
  }

  public void setRegisterType(RegisterType registerType) {
    this.registerType = registerType;
  }

  public AlarmType getAlarmType() {
    return alarmType;
  }

  public void setAlarmType(AlarmType alarmType) {
    this.alarmType = alarmType;
  }

  public AlarmItem getAlarmItem() {
    return alarmItem;
  }

  public void setAlarmItem(AlarmItem alarmItem) {
    this.alarmItem = alarmItem;
  }
}
