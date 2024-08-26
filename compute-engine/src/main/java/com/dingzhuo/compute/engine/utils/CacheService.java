package com.dingzhuo.compute.engine.utils;

import com.dingzhuo.compute.engine.message.alarm.AlarmStatus;
import com.dingzhuo.compute.engine.message.device.DeviceStatus;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.model.domain.LimitType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CacheService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private ConcurrentHashMap<String, IndexStorage> indexStorageCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, List<String>> postIndexIdsCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, AlarmItem> alarmItemCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, AlarmStatus> alarmCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, LimitType> limitTypeCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, TagValue> tagValueCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, DeviceFormula> deviceFormulaCache = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, DeviceStatus> deviceStatusCache = new ConcurrentHashMap<>();

  private ConcurrentHashMap<TimeType, Set<String>> registers = new ConcurrentHashMap<>();

  /**
   * 缓存指标存储信息.
   *
   * @param indexStorage 指标存储
   */
  public void cacheIndexStorage(IndexStorage indexStorage) {
    String actorId = ActorUtil.buildActorId(indexStorage);
    indexStorageCache.put(actorId, indexStorage);
  }

  /**
   * 根据 actorId 获取指标存储.
   *
   * @param actorId
   * @return
   */
  public IndexStorage getIndexStorageCache(String actorId) {
    return indexStorageCache.getOrDefault(actorId, null);
  }

  /**
   * 缓存计算指标的后置指标.
   *
   * @param actorId
   * @param postId
   */
  public void cachePostIndex(String actorId, String postId) {
    if (!postIndexIdsCache.containsKey(actorId)) {
      postIndexIdsCache.put(actorId, new ArrayList<>());
    }

    postIndexIdsCache.get(actorId).add(postId);
  }

  /**
   * 移除后置指标.
   *
   * @param actorId
   * @param postActorId
   */
  public void removePostIndexCache(String actorId, String postActorId) {
    if (!postIndexIdsCache.containsKey(actorId)) {
      postIndexIdsCache.put(actorId, new ArrayList<>());
    }

    postIndexIdsCache.get(actorId).remove(postActorId);
  }

  /**
   * 获取后置指标actorId.
   *
   * @param actorId
   * @return
   */
  public List<String> getPostActorIds(String actorId) {
    return postIndexIdsCache.getOrDefault(actorId, new ArrayList<>());
  }

  public void cacheAlarmItem(AlarmItem alarmItem) {
    String actorId = ActorUtil.buildAlarmActorId(alarmItem);
    alarmItemCache.put(actorId, alarmItem);
  }

  public void removeAlarmCache(String actorId) {
    alarmItemCache.remove(actorId);
  }

  public void removeIndexCache(String actorId) {
    indexStorageCache.remove(actorId);
  }

  public AlarmItem getAlarmItem(String actorId) {
    return alarmItemCache.getOrDefault(actorId, null);
  }

  public void cacheAlarmStatus(String indexId, String timeType, String limitType,
      AlarmStatus status) {
    String statusId = ActorUtil.buildAlarmStatusId(indexId, timeType, limitType);
    alarmCache.put(statusId, status);
  }

  public void cacheAlarmStatus(String statusId, AlarmStatus status) {
    alarmCache.put(statusId, status);
  }

  public AlarmStatus getAlarmStatus(String indexId, String timeType, String limitType) {
    String statusId = ActorUtil.buildAlarmStatusId(indexId, timeType, limitType);
    return alarmCache.getOrDefault(statusId, null);
  }

  public void cacheTagValues(List<TagValue> tagValues) {
    tagValues.forEach(tagValue -> tagValueCache.put(tagValue.getTagCode(), tagValue));
  }

  public void cacheLimitType(String limitCode, LimitType type) {
    limitTypeCache.put(limitCode, type);
  }

  public LimitType getLimitType(String limitType) {
    return limitTypeCache.getOrDefault(limitType, null);
  }

  public void cacheTagValue(TagValue tagValue) {
    if (tagValue == null) {
      return;
    }

    tagValueCache.put(tagValue.getTagCode(), tagValue);
  }

  public TagValue getTagValue(String indexCode) {
    if (StringUtils.isBlank(indexCode)) {
      return null;
    }

    return tagValueCache.getOrDefault(indexCode, null);
  }

  public void cacheDeviceStatusSetting(DeviceFormula deviceFormula) {
    if (deviceFormula != null) {
      String actorId = ActorUtil.buildActorId(deviceFormula);
      deviceFormulaCache.put(actorId, deviceFormula);
    }
  }

  public void removeDeviceStatusSetting(String actorId) {
    deviceFormulaCache.remove(actorId);
  }

  public DeviceFormula getDeviceFormula(String actorId) {
    return deviceFormulaCache.getOrDefault(actorId, null);
  }

  public DeviceStatus getDeviceStatus(String actorId) {
    return deviceStatusCache.getOrDefault(actorId, null);
  }

  public void cacheDeviceStatus(String actorId, DeviceStatus lastStatus) {
    deviceStatusCache.put(actorId, lastStatus);
  }

  public ConcurrentHashMap<TimeType, Set<String>> getRegisters() {
    return registers;
  }
}
