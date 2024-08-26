package com.dingzhuo.compute.engine.actor.device;

import akka.actor.UntypedAbstractActor;
import com.dingzhuo.compute.engine.function.FunctionEngine;
import com.dingzhuo.compute.engine.message.device.DeviceStatus;
import com.dingzhuo.compute.engine.message.device.DeviceStatusJudgeMessage;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusHistory;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusLive;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusHistoryService;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusLiveService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("deviceStatusActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceStatusActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "deviceStatusActor";
  private final CacheService cacheService;
  private final IDeviceStatusLiveService liveService;
  private final IDeviceStatusHistoryService historyService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public DeviceStatusActor(CacheService cacheService,
      IDeviceStatusLiveService liveService,
      IDeviceStatusHistoryService historyService) {
    this.cacheService = cacheService;
    this.liveService = liveService;
    this.historyService = historyService;
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof DeviceStatusJudgeMessage) {
      judge((DeviceStatusJudgeMessage) message);
    }
  }

  private void judge(DeviceStatusJudgeMessage message) {
    try {
      String actorId = message.getActorId();
      DeviceFormula formula = cacheService.getDeviceFormula(actorId);
      if (formula == null) {
        return;
      }

      DeviceStatus lastStatus = cacheService.getDeviceStatus(actorId);
      DateTime now = DateTime.now();
      if (lastStatus == null) {
        lastStatus = new DeviceStatus();
        lastStatus.setAlarm(false);
        lastStatus.setBeginTime(now.toDate());
      }

      Object value = FunctionEngine.getInstance().eval(formula.getCalcText());
      boolean isAlarm = (Boolean) value;
      if (isAlarm != lastStatus.isAlarm()) {
        lastStatus.setAlarm(isAlarm);
        cacheService.cacheDeviceStatus(actorId, lastStatus);
        if (isAlarm) {
          DeviceStatusLive live = new DeviceStatusLive();
          live.setId(UUID.fastUUID().toString());
          live.setDeviceId(formula.getDeviceId());
          live.setStatusId(formula.getStateId());
          live.setBeginTime(now.toString("yyyy-MM-dd HH:mm:ss"));
          liveService.insertDeviceStatusLive(live);
        } else {
          DeviceStatusHistory history = new DeviceStatusHistory();
          DeviceStatusLive live = liveService
              .getDeviceStatus(formula.getDeviceId(), formula.getStateId());
          history.setId(UUID.fastUUID().toString());
          history.setDeviceId(formula.getDeviceId());
          history.setStatusId(formula.getStateId());
          history.setBeginTime(live.getBeginTime());
          history.setEndTime(now.toString("yyyy-MM-dd HH:mm:ss"));
          historyService.saveHistoryStatus(history);
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
  }
}
