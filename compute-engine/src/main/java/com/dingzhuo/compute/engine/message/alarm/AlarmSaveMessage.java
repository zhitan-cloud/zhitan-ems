package com.dingzhuo.compute.engine.message.alarm;

import com.dingzhuo.compute.engine.message.BaseActorMessage;

/**
 * @author fanxinfu
 */
public class AlarmSaveMessage extends BaseActorMessage {

  public AlarmSaveMessage(String actorId) {
    super(actorId);
  }
}
