package com.dingzhuo.compute.engine.actor.device;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import com.dingzhuo.compute.engine.actor.alarm.PeriodAlarmActor;
import com.dingzhuo.compute.engine.message.device.DeviceStatusJudgeMessage;
import com.dingzhuo.compute.engine.message.device.LoadDeviceStatusMessage;
import com.dingzhuo.compute.engine.message.device.UnloadDeviceStatusMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component("deviceStatusTimerActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceStatusTimerActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "deviceStatusTimerActor";
  private List<String> actorIds = new ArrayList<>();
  private final ActorSystem actorSystem;
  private final CacheService cacheService;
  private ActorSelection deviceStatusActor;

  public DeviceStatusTimerActor(ActorSystem actorSystem, CacheService cacheService) {
    this.actorSystem = actorSystem;
    this.cacheService = cacheService;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();

    deviceStatusActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(DeviceStatusActor.ACTOR_NAME));
    FiniteDuration interval = Duration.create(30, TimeUnit.SECONDS);
    FiniteDuration delay = Duration.Zero();
    actorSystem.scheduler()
        .scheduleAtFixedRate(delay, interval, self(), Message.TIMER, actorSystem.dispatcher(),
            self());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof LoadDeviceStatusMessage) {
      LoadDeviceStatusMessage loadMessage = (LoadDeviceStatusMessage) message;
      actorIds.add(loadMessage.getActorId());
      cacheService.cacheDeviceStatusSetting(loadMessage.getDeviceFormula());
    } else if (message instanceof UnloadDeviceStatusMessage) {
      UnloadDeviceStatusMessage unloadMessage = (UnloadDeviceStatusMessage) message;
      actorIds.remove(unloadMessage.getActorId());
      cacheService.removeDeviceStatusSetting(unloadMessage.getActorId());
    } else if (message == Message.TIMER) {
      doTimer();
    }
  }

  private void doTimer() {
    if (!actorIds.isEmpty()) {
      actorIds.forEach(id -> deviceStatusActor.tell(new DeviceStatusJudgeMessage(id), getSelf()));
    }
  }

  enum Message {
    /**
     * 到点触发
     */
    TIMER
  }
}
