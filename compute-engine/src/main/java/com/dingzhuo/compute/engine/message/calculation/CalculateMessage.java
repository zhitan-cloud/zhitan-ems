package com.dingzhuo.compute.engine.message.calculation;

import com.dingzhuo.compute.engine.message.ExecuteType;
import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.energy.common.utils.time.TimeType;

/**
 * @author fanxinfu
 */
public class CalculateMessage extends BaseActorMessage {

  private String timeCode;
  private TimeType timeType;
  private ExecuteType executeType;

  public CalculateMessage(String actorId, String timeCode, TimeType timeType,
      ExecuteType executeType) {
    super(actorId);
    this.timeCode = timeCode;
    this.timeType = timeType;
    this.executeType = executeType;
  }

  public String getTimeCode() {
    return timeCode;
  }

  public void setTimeCode(String timeCode) {
    this.timeCode = timeCode;
  }

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public ExecuteType getExecuteType() {
    return executeType;
  }

  public void setExecuteType(ExecuteType executeType) {
    this.executeType = executeType;
  }
}
