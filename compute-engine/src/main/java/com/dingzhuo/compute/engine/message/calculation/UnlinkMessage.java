package com.dingzhuo.compute.engine.message.calculation;

import com.dingzhuo.compute.engine.message.BaseActorMessage;

public class UnlinkMessage extends BaseActorMessage {

  private String postActorId;

  public UnlinkMessage(String actorId, String postActorId) {
    super(actorId);
    this.postActorId = postActorId;
  }

  public String getPostActorId() {
    return postActorId;
  }

  public void setPostActorId(String postActorId) {
    this.postActorId = postActorId;
  }
}
