package com.dingzhuo.compute.engine.utils;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component
public class SpringAkkaExtension implements Extension {

  private ApplicationContext applicationContext;

  public void initialize(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public Props props(String actorBeanName) {
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
  }
}
