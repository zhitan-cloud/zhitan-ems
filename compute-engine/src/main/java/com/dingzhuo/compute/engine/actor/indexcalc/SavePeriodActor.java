package com.dingzhuo.compute.engine.actor.indexcalc;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dingzhuo.compute.engine.message.ExecuteType;
import com.dingzhuo.compute.engine.message.calculation.CalculateMessage;
import com.dingzhuo.compute.engine.message.save.SaveMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.ServiceProvicer;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component("savePeriodActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SavePeriodActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "savePeriodActor";
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final ActorSystem actorSystem;
  private Cancellable timer;
  private ActorSelection calculateActor;
  private ConcurrentHashMap<String, SaveMessage> cacheData = new ConcurrentHashMap<>();

  public SavePeriodActor(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    calculateActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    FiniteDuration interval = Duration.create(10, TimeUnit.SECONDS);
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
    if (message instanceof Message) {
      if (message == Message.TIMER) {
        doSave();
      }
    } else if (message instanceof SaveMessage) {
      SaveMessage saveMessage = (SaveMessage) message;
      String key = String.format("%s:%s", saveMessage.getIndexId(), saveMessage.getTimeCode());
      cacheData.put(key, saveMessage);
    }
  }

  private void doSave() {
    if (!cacheData.isEmpty()) {
      List<SaveMessage> saveData = new ArrayList<>();
      List<String> needRemoveKeys = new ArrayList<>();
      List<CalculateMessage> postIndexMessage = new ArrayList<>();
      cacheData.forEach((key, value) -> {
        needRemoveKeys.add(key);
        saveData.add(value);
        value.getPostActorIds().forEach(actorId -> {
          if (!equalsIgnoreCase(actorId,
              ActorUtil.buildActorId(value.getIndexId(), value.getTimeType()))) {
            CalculateMessage message =
                new CalculateMessage(actorId, value.getTimeCode(),
                    value.getTimeType(),
                    ExecuteType.TIMER);
            postIndexMessage.add(message);
          }
        });
      });

      for (String key : needRemoveKeys) {
        cacheData.remove(key);
      }

      savePeriodData(saveData);
      postIndexMessage.forEach(message -> calculateActor.tell(message, getSelf()));
    }
  }

  private void savePeriodData(List<SaveMessage> saveData) {
    List<DataItem> dataItems = new ArrayList<>();
    saveData.forEach(data -> {
      String timeCode = data.getTimeCode();
      DataItem dataItem = new DataItem();
      dataItem.setIndexId(data.getIndexId());
      dataItem.setTimeCode(timeCode);
      dataItem.setTimeType(data.getTimeType());
      dataItem.setBeginTime(TimeManager.getBeginTime(timeCode));
      dataItem.setEndTime(TimeManager.getEndTime(timeCode));
      dataItem.setDataTime(TimeManager.getTime(timeCode));
      dataItem.setValue(data.getValue());
      dataItems.add(dataItem);
    });

    try {
      ServiceProvicer.getPeriodDataService().save(dataItems);
    } catch (Exception ex) {
      log.error("批量保存失败！" + ex.getMessage());
      dataItems.forEach(item -> {
        try {
          if (item != null) {
            ServiceProvicer.getPeriodDataService().save(item);
          }
        } catch (Exception singleEx) {
          log.error("单个保存失败！" + singleEx.getMessage());
        }
      });
    }

    log.error(DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS"));
  }

  private enum Message {
    /**
     * 时间触发
     */
    TIMER
  }
}
