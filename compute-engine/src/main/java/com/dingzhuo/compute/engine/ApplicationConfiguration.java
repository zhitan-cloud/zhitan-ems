package com.dingzhuo.compute.engine;

import akka.actor.ActorSystem;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.SpringAkkaExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fanxinfu
 */
@Configuration
public class ApplicationConfiguration {

  private final ApplicationContext applicationContext;
  private final SpringAkkaExtension springAkkaExtension;

  public ApplicationConfiguration(
      ApplicationContext applicationContext, SpringAkkaExtension springAkkaExtension) {
    this.applicationContext = applicationContext;
    this.springAkkaExtension = springAkkaExtension;
  }

  @Bean
  public ActorSystem actorSystem() {
    ActorSystem system = ActorSystem.create(ActorUtil.ACTOR_SYSTEM, akkaConfiguration());
    springAkkaExtension.initialize(applicationContext);
    system.dispatchers().lookup("computeDispatcher");
    return system;
  }

  @Bean
  public Config akkaConfiguration() {
    return ConfigFactory.load("akka");
  }

}
