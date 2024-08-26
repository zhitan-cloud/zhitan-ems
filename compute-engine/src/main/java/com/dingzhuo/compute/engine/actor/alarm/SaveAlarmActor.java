package com.dingzhuo.compute.engine.actor.alarm;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author fanxinfu
 */
@Component("saveAlarmActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SaveAlarmActor extends UntypedAbstractActor {

  private Cancellable timer;
  private final ActorSystem actorSystem;

  public SaveAlarmActor(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    FiniteDuration interval = Duration.create(1, TimeUnit.SECONDS);
    FiniteDuration delay = Duration.Zero();
    timer = actorSystem.scheduler()
        .scheduleAtFixedRate(delay, interval, self(), Message.TIMER, actorSystem.dispatcher(),
            self());
  }

  @Override
  public void postStop() throws Exception {
    super.postStop();
    if (timer != null) {
      timer.cancel();
    }
  }

  @Override
  public void onReceive(Object message) throws Throwable {

  }

  private enum Message {
    /**
     * 时间触发
     */
    TIMER
  }
}
