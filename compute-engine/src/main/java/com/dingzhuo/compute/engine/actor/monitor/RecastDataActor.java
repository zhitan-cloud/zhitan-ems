package com.dingzhuo.compute.engine.actor.monitor;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import com.alibaba.fastjson.JSONArray;
import com.dingzhuo.compute.engine.actor.indexcalc.CalculationIndexActor;
import com.dingzhuo.compute.engine.message.ExecuteType;
import com.dingzhuo.compute.engine.message.calculation.CalculateMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author fanxinfu
 */
@Component("recastDataActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecastDataActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "recastDataActor";
  private final CacheService cacheService;
  private Cancellable timerCancelable;
  private final ActorSystem actorSystem;
  private ActorSelection calculationActor;
  Logger logger = LoggerFactory.getLogger(RecastDataActor.class.getName());

  public RecastDataActor(ActorSystem actorSystem, CacheService cacheService) {
    this.actorSystem = actorSystem;
    this.cacheService = cacheService;
  }

  @Override
  public void preStart() {
    calculationActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    FiniteDuration interval = Duration.create(10, TimeUnit.SECONDS);
    FiniteDuration delay = Duration.Zero();
    this.timerCancelable = actorSystem.scheduler().scheduleAtFixedRate(delay,
        interval,
        self(),
        Message.TIMER,
        actorSystem.dispatcher(),
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
        verifyRecastData();
      }
    }
  }

  private void verifyRecastData() {
    String pathname = "recast.json";
    try {
      File jsonFile = new File(pathname);
      if (jsonFile.exists()) {
        sendRecastData(jsonFile);
      }
    } catch (Exception e) {
      logger.error("", e);
    }

  }

  private void sendRecastData(File jsonFile) {
    try {
      String jsonString = FileUtils.readFileToString(jsonFile, Charset.defaultCharset());
      List<RecastConfig> configs = JSONArray.parseArray(jsonString, RecastConfig.class);
      configs.forEach(config -> {
        DateTime time = config.getBeginTime();
        TimeType timeType = config.getTimeType();
        Set<String> actorIds = cacheService.getRegisters().get(timeType);
        if (actorIds != null && actorIds.size() > 0) {
          while (time.isBefore(config.getEndTime())) {
            String timeCode = TimeManager.getTimeCode(time.toDate(), timeType);
            for (String actorId : actorIds) {
              CalculateMessage msg =
                  new CalculateMessage(actorId, timeCode, timeType, ExecuteType.TIMER);
              calculationActor.tell(msg, getSelf());
            }

            if (timeType == TimeType.HOUR) {
              time = time.plusHours(1);
            } else if (timeType == TimeType.DAY) {
              time = time.plusDays(1);
            } else if (timeType == TimeType.MONTH) {
              time = time.plusMonths(1);
            } else if (timeType == TimeType.YEAR) {
              time = time.plusYears(1);
            }

            try {
              Thread.sleep(0);
            } catch (InterruptedException e) {
              logger.error("", e);
            }
          }
        }
      });

      jsonFile.delete();
    } catch (Exception ex) {
      logger.error("", ex);
    }
  }

  private static class RecastConfig {

    private DateTime beginTime;
    private DateTime endTime;
    private TimeType timeType;

    public DateTime getBeginTime() {
      return beginTime;
    }

    public void setBeginTime(DateTime beginTime) {
      this.beginTime = beginTime;
    }

    public DateTime getEndTime() {
      return endTime;
    }

    public void setEndTime(DateTime endTime) {
      this.endTime = endTime;
    }

    public TimeType getTimeType() {
      return timeType;
    }

    public void setTimeType(TimeType timeType) {
      this.timeType = timeType;
    }
  }

  public enum Message {
    /**
     * 时间触发
     */
    TIMER
  }
}
