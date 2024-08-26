package com.dingzhuo.compute.engine.message.alarm;

import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;

/**
 * @author fanxinfu
 */
public class LoadAlarmMessage extends BaseActorMessage {

  private AlarmItem alarmItem;

  public LoadAlarmMessage(AlarmItem alarmItem) {
    super(ActorUtil.buildAlarmActorId(alarmItem));
    this.alarmItem = alarmItem;
  }

  public AlarmItem getAlarmItem() {
    return alarmItem;
  }

  public void setAlarmItem(AlarmItem alarmItem) {
    this.alarmItem = alarmItem;
  }
}
