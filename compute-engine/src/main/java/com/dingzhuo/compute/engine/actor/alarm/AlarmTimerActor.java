package com.dingzhuo.compute.engine.actor.alarm;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import com.dingzhuo.compute.engine.message.alarm.AlarmJudgeMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmRegisterMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmType;
import com.dingzhuo.compute.engine.message.timer.RegisterType;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component("alarmTimerActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlarmTimerActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "alarmTimerActor";
  private final ActorSystem actorSystem;
  private Cancellable timer;
  private Map<AlarmType, List<String>> actorIds = new HashMap<>();
  private List<String> realtimeTags = new ArrayList<>();
  private ActorSelection realtimeAlarmActor;
  private ActorSelection periodAlarmActor;
  private final RealtimeDatabaseService realtimeDatabaseService;
  private CacheService cacheService;

  public AlarmTimerActor(ActorSystem actorSystem, RealtimeDatabaseService realtimeDatabaseService,
      CacheService cacheService) {
    this.actorSystem = actorSystem;
    this.realtimeDatabaseService = realtimeDatabaseService;
    this.cacheService = cacheService;
  }

  @Override
  public void preStart() {
    realtimeAlarmActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(RealtimeAlarmActor.ACTOR_NAME));
    periodAlarmActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(PeriodAlarmActor.ACTOR_NAME));
    FiniteDuration interval = Duration.create(30, TimeUnit.SECONDS);
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
  public void onReceive(Object message) {
    if (message == Message.TIMER) {
      timer();
    } else if (message instanceof AlarmRegisterMessage) {
      registerAlarm((AlarmRegisterMessage) message);
    }
  }

  private void registerAlarm(AlarmRegisterMessage registerMessage) {
    AlarmType alarmType = registerMessage.getAlarmType();
    if (!actorIds.containsKey(alarmType)) {
      actorIds.put(alarmType, new ArrayList<>());
    }

    if (registerMessage.getRegisterType() == RegisterType.REGISTER) {
      actorIds.get(alarmType).add(registerMessage.getActorId());
      if (alarmType == AlarmType.LIVE) {
        realtimeTags.add(registerMessage.getAlarmItem().getIndexCode());
      }
    } else if (registerMessage.getRegisterType() == RegisterType.UNREGISTER) {
      actorIds.get(alarmType).remove(registerMessage.getActorId());
      if (alarmType == AlarmType.LIVE) {
        realtimeTags.remove(registerMessage.getAlarmItem().getIndexCode());
      }
    }
  }

  private void timer() {
    if (!actorIds.isEmpty()) {
      List<String> realtimeAlarmIds = actorIds.get(AlarmType.LIVE);
      List<String> periodAlarmIds = actorIds.get(AlarmType.PERIOD);
      if (!realtimeTags.isEmpty()) {
        List<TagValue> tagValues = realtimeDatabaseService.retrieve(realtimeTags);
        cacheService.cacheTagValues(tagValues);
      }

      if (realtimeAlarmIds != null && !realtimeAlarmIds.isEmpty()) {
        realtimeAlarmIds
            .forEach(id -> realtimeAlarmActor.tell(new AlarmJudgeMessage(id), getSender()));
      }

      if (periodAlarmIds != null && !periodAlarmIds.isEmpty()) {
        periodAlarmIds.forEach(id -> periodAlarmActor.tell(new AlarmJudgeMessage(id), getSender()));
      }
    }
  }

  private enum Message {
    /**
     * 时间触发
     */
    TIMER
  }
}
