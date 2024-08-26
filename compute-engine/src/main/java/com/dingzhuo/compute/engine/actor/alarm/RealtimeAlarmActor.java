package com.dingzhuo.compute.engine.actor.alarm;

import com.dingzhuo.compute.engine.function.FunctionEngine;
import com.dingzhuo.compute.engine.message.alarm.AlarmJudgeMessage;
import com.dingzhuo.compute.engine.message.alarm.AlarmStatus;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component("realtimeAlarmActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RealtimeAlarmActor extends BaseAlarmActor {

  public static final String ACTOR_NAME = "realtimeAlarmActor";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  void judge(AlarmJudgeMessage message) {
    try {
      String actorId = message.getActorId();
      AlarmItem item = cacheService.getAlarmItem(actorId);
      AlarmStatus lastStatus = cacheService
          .getAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType());
      DateTime now = DateTime.now();
      if (lastStatus == null) {
        lastStatus = new AlarmStatus();
        lastStatus.setAlarm(false);
        lastStatus.setBeginTime(now.toDate());
      }

      Object value = FunctionEngine.getInstance().eval(item.getCalcText());
      boolean isAlarm = (Boolean) value;
      if (isAlarm != lastStatus.isAlarm()) {
        try {
          if (isAlarm) {
            TagValue tagValue = cacheService.getTagValue(item.getIndexCode());
            insertRealtimeAlarm(item, now, tagValue.getValue(), tagValue.getQuality(), "L");
          } else {
            insertHistoryAlarm(item, now);
          }

          lastStatus.setAlarm(isAlarm);
          cacheService
              .cacheAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType(),
                  lastStatus);
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }
}
