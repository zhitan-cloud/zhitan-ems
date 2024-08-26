package com.dingzhuo.compute.engine.actor.alarm;

import com.dingzhuo.compute.engine.function.FunctionEngine;
import com.dingzhuo.compute.engine.message.alarm.AlarmJudgeMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmStatus;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.domain.RealTimeAlarm;
import com.greenpineyu.fel.context.FelContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component("periodAlarmActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PeriodAlarmActor extends BaseAlarmActor {

  public static final String ACTOR_NAME = "periodAlarmActor";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  void judge(AlarmJudgeMessage message) {
    try {
      String actorId = message.getActorId();
      AlarmItem item = cacheService.getAlarmItem(actorId);
      TimeType timeType = TimeType.valueOf(item.getTimeSlot());
      String timeCode = TimeManager.getTimeCode(DateTime.now().toDate(), timeType);
      String lastTimeCode = TimeManager.getTimeCode(TimeManager.getTime(timeCode, -1), timeType);
      RealTimeAlarm alarm = realtimeAlarmService
          .getAlarmByItemIdAndTimeCode(item.getId(), lastTimeCode);
      if (alarm != null) {
        insertPeriodHistoryAlarm(item, new DateTime(TimeManager.getEndTime(lastTimeCode)),
            lastTimeCode, alarm);
      }

      AlarmStatus lastStatus = cacheService.getAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType());
      DateTime now = DateTime.now();
      if (lastStatus == null) {
        lastStatus = new AlarmStatus();
        lastStatus.setAlarm(false);
        lastStatus.setBeginTime(now.toDate());
      }

      FelContext context = FunctionEngine.getInstance().getContext();
      context.set("timeCode", timeCode);
      Object value = FunctionEngine.getInstance().eval(item.getCalcText(), context);
      boolean isAlarm = (Boolean) value;
      if (isAlarm != lastStatus.isAlarm()) {
        lastStatus.setAlarm(isAlarm);
        cacheService.cacheAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType(), lastStatus);
        if (isAlarm) {
          insertRealtimeAlarm(item, now, null, null, timeCode);
        } else {
          insertPeriodHistoryAlarm(item, now, timeCode, null);
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }
}
