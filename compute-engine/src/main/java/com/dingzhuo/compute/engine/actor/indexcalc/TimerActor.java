package com.dingzhuo.compute.engine.actor.indexcalc;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dingzhuo.compute.engine.message.ExecuteType;
import com.dingzhuo.compute.engine.message.calculation.CalculateMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterTimeMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterType;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.compute.engine.utils.ServiceProvicer;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author fanxinfu
 */
@Component("timerActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimerActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "timerActor";
  private final CacheService cacheService;
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final ActorSystem actorSystem;
  private Cancellable timerCancelable;
  private ActorSelection calculationActor;

  public TimerActor(ActorSystem actorSystem, CacheService cacheService) {
    this.actorSystem = actorSystem;
    this.cacheService = cacheService;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    calculationActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    FiniteDuration interval = Duration
        .create(ServiceProvicer.getCalculationConfig().getInterval(), TimeUnit.SECONDS);
    FiniteDuration delay = Duration.Zero();
    this.timerCancelable = actorSystem.scheduler()
        .scheduleAtFixedRate(delay, interval, self(), Message.TIMER, actorSystem.dispatcher(),
            self());
  }

  @Override
  public void postStop() throws Exception {
    super.postStop();
    if (this.timerCancelable != null) {
      this.timerCancelable.cancel();
    }
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof Message) {
      if (message == Message.TIMER) {
        doTimer();
      }
    } else if (message instanceof RegisterTimeMessage) {
      register((RegisterTimeMessage) message);
    }
  }

  private void register(RegisterTimeMessage message) {
    if (!cacheService.getRegisters().containsKey(message.getTimeType())) {
      cacheService.getRegisters().put(message.getTimeType(), new HashSet<>());
    }

    if (message.getRegisterType() == RegisterType.REGISTER) {
      cacheService.getRegisters().get(message.getTimeType()).add(message.getActorId());
    } else if (message.getRegisterType() == RegisterType.UNREGISTER) {
      cacheService.getRegisters().get(message.getTimeType()).remove(message.getActorId());
    }
  }

  private void doTimer() {
    Date now = DateTime.now().toDate();
    TimeType[] timeTypes = TimeManager.typeArray;
    int delayTime = ServiceProvicer.getCalculationConfig().getInterval() * 3;
    for (TimeType timeType : timeTypes) {
      String timeCode = TimeManager.getExecuteTimeCode(now, timeType, delayTime);
      if (StringUtils.isEmpty(timeCode)) {
        continue;
      }

      Set<String> actorIds = cacheService.getRegisters().get(timeType);
      if (actorIds == null || actorIds.isEmpty()) {
        continue;
      }

      for (String actorId : actorIds) {
        calculationActor
            .tell(new CalculateMessage(actorId, timeCode, timeType, ExecuteType.TIMER), getSelf());
      }
    }
  }

  public enum Message {
    /**
     * 时间触发
     */
    TIMER
  }
}
