package com.dingzhuo.compute.engine.actor.alarm;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

import akka.actor.ActorSelection;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dingzhuo.compute.engine.message.alarm.AlarmStatus;
import com.dingzhuo.compute.engine.message.alarm.LoadAlarmMessage;
import com.dingzhuo.compute.engine.message.alarm.UnloadAlarmMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.LimitType;
import com.dingzhuo.energy.data.model.service.ILimitTypeService;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.domain.RealTimeAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.service.IAlarmItemService;
import com.dingzhuo.energy.data.monitoring.alarm.service.IRealtimeAlarmService;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

/**
 * @author fanxinfu
 */
@Component("loadAlarmActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoadAlarmActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "loadAlarmActor";
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final IAlarmItemService alarmItemService;
  private final ILimitTypeService limitTypeService;
  private ActorSelection realtimeAlarm;
  private ActorSelection periodAlarm;
  private CacheService cacheService;
  private IRealtimeAlarmService realtimeAlarmService;
  Map<String, AlarmItem> loadedAlarmItem = new HashMap<>();

  public LoadAlarmActor(
      IAlarmItemService alarmItemService,
      ILimitTypeService limitTypeService,
      IRealtimeAlarmService realtimeAlarmService,
      CacheService cacheService) {
    this.alarmItemService = alarmItemService;
    this.limitTypeService = limitTypeService;
    this.cacheService = cacheService;
    this.realtimeAlarmService = realtimeAlarmService;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    realtimeAlarm = getContext()
        .actorSelection(ActorUtil.getActorAddress(RealtimeAlarmActor.ACTOR_NAME));
    periodAlarm = getContext()
        .actorSelection(ActorUtil.getActorAddress(PeriodAlarmActor.ACTOR_NAME));
    this.context().system().scheduler()
        .scheduleAtFixedRate(Duration.Zero(), Duration.create(1, TimeUnit.MINUTES), this.self(),
            Message.REFRESH, this.context().system().dispatcher(), null);
    cacheLimitType();
    initAlarm();
  }

  private void cacheLimitType() {
    List<LimitType> limitTypes = limitTypeService.selectLimitTypeList(new LimitType());
    limitTypes.forEach(type -> cacheService.cacheLimitType(type.getLimitCode(), type));
  }

  private void initAlarm() {
    List<RealTimeAlarm> alarms = realtimeAlarmService.getRealTimeAlarm();
    if (!alarms.isEmpty()) {
      alarms.forEach(alarm -> {
        AlarmStatus status = new AlarmStatus();
        status.setAlarm(true);
        status.setBeginTime(alarm.getBeginTime());
        cacheService.cacheAlarmStatus(alarm.getItemId(), status);
      });
    }
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof Message) {
      if (message == Message.REFRESH) {
        refreshAlarmItem();
      } else {
        this.unhandled(message);
      }
    }
  }

  private void refreshAlarmItem() {
    List<AlarmItem> alarmItemList = alarmItemService.getAllAlarmItem();
    Map<String, AlarmItem> newAlarmItem = alarmItemList.stream()
        .collect(Collectors.toMap(ActorUtil::buildAlarmActorId, item -> item));

    Set<String> needInstall = new HashSet<>();
    Set<String> needUninstall = new HashSet<>();

    loadedAlarmItem.forEach((id, alarmItem) -> {
      if (!newAlarmItem.containsKey(id)) {
        needUninstall.add(id);
      } else {
        Date nowUpdate = newAlarmItem.get(id).getUpdateTime();
        Date lastUpdate = alarmItem.getUpdateTime();
        if (lastUpdate != null && nowUpdate != null && lastUpdate.after(nowUpdate)) {
          needUninstall.add(id);
          needInstall.add(id);
        }
      }
    });

    newAlarmItem.forEach((id, alarmItem) -> {
      if (!loadedAlarmItem.containsKey(id)) {
        needInstall.add(id);
      }
    });

    needUninstall.forEach(id -> {
      AlarmItem alarmItem = loadedAlarmItem.get(id);
      loadedAlarmItem.remove(id);
      if (equalsAnyIgnoreCase(alarmItem.getTimeSlot(), TimeType.LIVE.name())) {
        realtimeAlarm.tell(new UnloadAlarmMessage(alarmItem), getSelf());
      } else {
        periodAlarm.tell(new UnloadAlarmMessage(alarmItem), getSelf());
      }
    });

    needInstall.forEach(id -> {
      AlarmItem alarmItem = newAlarmItem.get(id);
      if (equalsAnyIgnoreCase(alarmItem.getTimeSlot(), TimeType.LIVE.name())) {
        alarmItem.setCalcText("limitRealtimeAlarm('" + id + "')");
        realtimeAlarm.tell(new LoadAlarmMessage(alarmItem), getSelf());
      } else {
        alarmItem.setCalcText("limitPeriodAlarm('" + id + "')");
        periodAlarm.tell(new LoadAlarmMessage(alarmItem), getSelf());
      }

      loadedAlarmItem.put(id, alarmItem);
    });
  }

  public enum Message {
    /**
     * 检测报警配置
     */
    REFRESH
  }
}
