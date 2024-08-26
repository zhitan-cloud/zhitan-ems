package com.dingzhuo.compute.engine.actor.indexcalc;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import akka.cluster.sharding.ClusterSharding;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dingzhuo.compute.engine.function.FunctionEngine;
import com.dingzhuo.compute.engine.message.calculation.LoadCalcIndexMessage;
import com.dingzhuo.compute.engine.message.calculation.UnloadCalcIndexMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.model.service.IIndexStorageService;
import com.greenpineyu.fel.parser.FelNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

/**
 * @author fanxinfu
 */
@Component("loadIndexActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoadIndexActor extends UntypedAbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final IIndexStorageService indexStorageService;
  public static final String ACTOR_NAME = "loadIndexActor";
  private Map<String, IndexStorage> loadedCalcIndex = new HashMap<>();
  private ActorSelection calculateActor;

  public LoadIndexActor(IIndexStorageService indexStorageService) {
    this.indexStorageService = indexStorageService;
  }

  @Override
  public void preStart() {
    calculateActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    this.context().system().scheduler()
        .scheduleAtFixedRate(Duration.Zero(), Duration.create(5, TimeUnit.MINUTES), this.self(),
            Message.REFRESH, this.context().system().dispatcher(), null);
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof Message) {
      if (message == Message.REFRESH) {
        refreshIndex();
      } else {
        this.unhandled(message);
      }
    }
  }

  private void refreshIndex() {
    List<IndexStorage> indexStorages = indexStorageService.getAllCalcIndexStorage();
    List<IndexStorage> filterIndexStorageList = new ArrayList<>();
    indexStorages.forEach(indexStorage -> {
      try {
        if (StringUtils.isNotBlank(indexStorage.getCalcText())) {
          FelNode node = FunctionEngine.getInstance().parse(indexStorage.getCalcText());
          if (node != null && !node.getChildren().isEmpty()) {
            filterIndexStorageList.add(indexStorage);
          } else {
            log.error("ErrorIndex:" + indexStorage.getId() + ";" + indexStorage.getCalcText());
          }
        }
      } catch (Exception ex) {
        log.error("ErrorIndex:" + indexStorage.getId() + ";" + indexStorage.getCalcText());
      }
    });

    if (filterIndexStorageList.isEmpty()) {
      return;
    }

    Map<String, IndexStorage> newCalcIndex = filterIndexStorageList.stream()
        .collect(Collectors.toMap(IndexStorage::getId, indexStorage -> indexStorage));
    Set<String> needInstall = new HashSet<>();
    Set<String> needUninstall = new HashSet<>();

    loadedCalcIndex.forEach((id, indexStorage) -> {
      if (!newCalcIndex.containsKey(id)) {
        needUninstall.add(id);
      } else {
        Date nowUpdate = newCalcIndex.get(id).getUpdateTime();
        Date lastUpdate = indexStorage.getUpdateTime();
        if (lastUpdate != null && nowUpdate != null && lastUpdate.after(nowUpdate)) {
          needUninstall.add(id);
          needInstall.add(id);
        }
      }
    });

    newCalcIndex.forEach((id, indexStorage) -> {
      if (!loadedCalcIndex.containsKey(id)) {
        needInstall.add(id);
      }
    });

    needUninstall.forEach(id -> {
      IndexStorage indexStorage = loadedCalcIndex.get(id);
      loadedCalcIndex.remove(id);
      calculateActor
          .tell(new UnloadCalcIndexMessage(ActorUtil.buildActorId(indexStorage)), getSelf());
    });

    needInstall.forEach(id -> {
      IndexStorage indexStorage = newCalcIndex.get(id);
      loadedCalcIndex.put(id, indexStorage);
      calculateActor.tell(new LoadCalcIndexMessage(indexStorage), getSelf());
    });
  }

  public enum Message {
    /**
     * 检测指标
     */
    REFRESH
  }
}
