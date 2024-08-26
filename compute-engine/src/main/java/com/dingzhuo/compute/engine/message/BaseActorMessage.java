package com.dingzhuo.compute.engine.message;

/**
 * @author fanxinfu
 */
public class BaseActorMessage {

  private String actorId;

  public BaseActorMessage(String actorId) {
    this.actorId = actorId;
  }

  public String getActorId() {
    return actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }
}
