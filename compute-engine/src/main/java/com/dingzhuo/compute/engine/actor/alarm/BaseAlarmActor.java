package com.dingzhuo.compute.engine.actor.alarm;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

import akka.actor.ActorSelection;
import akka.actor.UntypedAbstractActor;
import com.dingzhuo.compute.engine.message.alarm.AlarmJudgeMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmRegisterMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmType;
import com.dingzhuo.compute.engine.message.alarm.LoadAlarmMessage;
import com.dingzhuo.compute.engine.message.alarm.UnloadAlarmMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterType;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.domain.HistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.RealTimeAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.service.IHistoryAlarmService;
import com.dingzhuo.energy.data.monitoring.alarm.service.IRealtimeAlarmService;
import com.dingzhuo.energy.dataservice.domain.Quality;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fanxinfu
 */
public abstract class BaseAlarmActor extends UntypedAbstractActor {

  @Autowired
  protected CacheService cacheService;
  @Autowired
  protected IRealtimeAlarmService realtimeAlarmService;
  @Autowired
  private IHistoryAlarmService historyAlarmService;

  private ActorSelection timerActor;

  @Override
  public void preStart() throws Exception {
    super.preStart();
    timerActor = getContext().actorSelection(ActorUtil.getActorAddress(AlarmTimerActor.ACTOR_NAME));
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof LoadAlarmMessage) {
      loadAlarm((LoadAlarmMessage) message);
    } else if (message instanceof UnloadAlarmMessage) {
      unloadAlarm((UnloadAlarmMessage) message);
    } else if (message instanceof AlarmJudgeMessage) {
      judge((AlarmJudgeMessage) message);
    }
  }

  private void unloadAlarm(UnloadAlarmMessage message) {
    cacheService.removeAlarmCache(message.getActorId());
    AlarmItem alarmItem = message.getAlarmItem();
    AlarmType alarmType = getAlarmType(alarmItem);
    timerActor
        .tell(new AlarmRegisterMessage(message.getActorId(), alarmType, RegisterType.UNREGISTER,
                alarmItem),
            self());
  }

  private void loadAlarm(LoadAlarmMessage message) {
    AlarmItem alarmItem = message.getAlarmItem();
    AlarmType alarmType = getAlarmType(alarmItem);
    cacheService.cacheAlarmItem(alarmItem);
    timerActor.tell(new AlarmRegisterMessage(message.getActorId(), alarmType, RegisterType.REGISTER,
        alarmItem), self());
  }

  @NotNull
  private AlarmType getAlarmType(AlarmItem alarmItem) {
    AlarmType alarmType;
    if (equalsAnyIgnoreCase(alarmItem.getTimeSlot(), TimeType.LIVE.name())) {
      alarmType = AlarmType.LIVE;
    } else {
      alarmType = AlarmType.PERIOD;
    }
    return alarmType;
  }

  protected void insertPeriodHistoryAlarm(AlarmItem item, DateTime now, String timeCode,
      RealTimeAlarm realTimeAlarm) {
    HistoryAlarm historyAlarm = new HistoryAlarm();
    if (realTimeAlarm == null) {
      realTimeAlarm = realtimeAlarmService.getAlarmByItemIdAndTimeCode(item.getId(), timeCode);
    }

    historyAlarm.setId(UUID.fastUUID().toString());
    historyAlarm.setBeginTime(realTimeAlarm.getBeginTime());
    historyAlarm.setIndexId(realTimeAlarm.getIndexId());
    historyAlarm.setLimitingValue(realTimeAlarm.getLimitingValue());
    historyAlarm.setItemId(item.getId());
    historyAlarm.setAlarmValue(realTimeAlarm.getAlarmValue());
    historyAlarm.setAlarmValueQuality(realTimeAlarm.getAlarmValueQuality());
    historyAlarm.setEndTime(now.toDate());
    historyAlarm.setTimeCode(realTimeAlarm.getTimeCode());
    int durationSecond = Seconds.secondsBetween(new DateTime(historyAlarm.getBeginTime()),
        new DateTime(historyAlarm.getEndTime())).getSeconds();
    historyAlarm.setDuration(durationSecond);
    historyAlarmService.updateHistoryAlarm(historyAlarm.getItemId(), historyAlarm);
  }

  protected void insertHistoryAlarm(AlarmItem item, DateTime now) {
    HistoryAlarm historyAlarm = new HistoryAlarm();
    RealTimeAlarm realTimeAlarm = realtimeAlarmService.getRealTimeAlarmByAlarmCode(
        item.getAlarmCode());
    if (realTimeAlarm == null) {
      return;
    }

    historyAlarm.setId(UUID.fastUUID().toString());
    historyAlarm.setBeginTime(realTimeAlarm.getBeginTime());
    historyAlarm.setIndexId(realTimeAlarm.getIndexId());
    historyAlarm.setLimitingValue(realTimeAlarm.getLimitingValue());

    historyAlarm.setItemId(item.getId());
    historyAlarm.setAlarmCode(item.getAlarmCode());
    historyAlarm.setAlarmValue(realTimeAlarm.getAlarmValue());
    historyAlarm.setAlarmValueQuality(realTimeAlarm.getAlarmValueQuality());
    historyAlarm.setEndTime(now.toDate());
    historyAlarm.setTimeCode(realTimeAlarm.getTimeCode());
    int durationSecond = Seconds.secondsBetween(new DateTime(historyAlarm.getBeginTime()),
        new DateTime(historyAlarm.getEndTime())).getSeconds();
    historyAlarm.setDuration(durationSecond);
    historyAlarmService.updateHistoryAlarm(historyAlarm.getAlarmCode(), historyAlarm);
  }

  protected void insertRealtimeAlarm(AlarmItem item, DateTime now, Double value, Quality quality,
      String timeCode) {
    RealTimeAlarm realTimeAlarm = realtimeAlarmService.getRealTimeAlarmByAlarmCode(
        item.getAlarmCode());
    if (realTimeAlarm != null) {
      return;
    }

    realTimeAlarm = new RealTimeAlarm();
    realTimeAlarm.setId(UUID.fastUUID().toString());
    realTimeAlarm.setBeginTime(now.toDate());
    realTimeAlarm.setIndexId(item.getDwid());
    realTimeAlarm.setLimitingValue(Double.parseDouble(item.getLimitVal()));

    realTimeAlarm.setItemId(item.getId());
    realTimeAlarm.setAlarmCode(item.getAlarmCode());
    realTimeAlarm.setAlarmValue(value);
    realTimeAlarm.setAlarmValueQuality(quality);
    realTimeAlarm.setTimeCode(timeCode);
    realtimeAlarmService.insert(realTimeAlarm);
  }

  abstract void judge(AlarmJudgeMessage message);
}
