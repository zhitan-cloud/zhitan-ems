package com.dingzhuo.compute.engine.config;

import akka.cluster.sharding.ShardRegion.MessageExtractor;
import com.dingzhuo.compute.engine.message.BaseActorMessage;

/**
 * @author fanxinfu
 */
public class CalcMessageExtractor implements MessageExtractor {

  @Override
  public String entityId(Object message) {
    if (message instanceof BaseActorMessage) {
      return ((BaseActorMessage) message).getActorId();
    }

    return null;
  }

  @Override
  public Object entityMessage(Object message) {
    return message;
  }

  @Override
  public String shardId(Object message) {
    if (message instanceof BaseActorMessage) {
      return String.valueOf(((BaseActorMessage) message).getActorId().hashCode() % 10);
    }

    return null;
  }
}
