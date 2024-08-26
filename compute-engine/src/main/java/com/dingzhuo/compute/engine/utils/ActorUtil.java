package com.dingzhuo.compute.engine.utils;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanxinfu
 */
public class ActorUtil {

  public static String ACTOR_SYSTEM = "CsieComputeEngine";

  public static String buildActorId(IndexStorage indexStorage) {
    return String
        .format("Index:%s:%s", indexStorage.getTimeType().name(), indexStorage.getIndexId());
  }

  public static String buildAlarmActorId(AlarmItem alarmItem) {
    return String.format("Alarm:%s:%s:%s", alarmItem.getDwid(), alarmItem.getTimeSlot(),
        alarmItem.getLimitType());
  }

  public static String buildActorId(String indexId, TimeType timeType) {
    return String.format("Index:%s:%s", timeType, indexId);
  }

  public static String buildAlarmStatusId(String indexId, String timeType, String limitType) {
    return String.format("Status:%s:%s:%s", indexId, timeType, limitType);
  }

  public static String getActorAddress(String actorName) {
    return String.format("akka://%s/user/%s", ACTOR_SYSTEM, actorName);
  }

  public static <E> List<List<E>> splitList(List<E> targetList, Integer splitSize) {
    if (targetList == null) {
      return new ArrayList<>();
    }

    int size = targetList.size();
    List<List<E>> resultList = new ArrayList<>();
    if (size <= splitSize) {
      resultList.add(targetList);
    } else {
      for (int i = 0; i < size; i += splitSize) {
        //用于限制最后一部分size小于splitSize的list
        int limit = i + splitSize;
        if (limit > size) {
          limit = size;
        }
        resultList.add(targetList.subList(i, limit));
      }
    }
    return resultList;
  }

  public static String buildActorId(DeviceFormula deviceFormula) {
    return String.format("Device:%s", deviceFormula.getId());
  }
}
