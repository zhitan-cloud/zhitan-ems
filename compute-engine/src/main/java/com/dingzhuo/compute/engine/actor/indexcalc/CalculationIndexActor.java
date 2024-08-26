package com.dingzhuo.compute.engine.actor.indexcalc;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import akka.cluster.sharding.ClusterSharding;
import com.dingzhuo.compute.engine.function.FunctionEngine;
import com.dingzhuo.compute.engine.message.calculation.CalculateMessage;
import com.dingzhuo.compute.engine.message.calculation.LinkMessage;
import com.dingzhuo.compute.engine.message.calculation.LoadCalcIndexMessage;
import com.dingzhuo.compute.engine.message.calculation.UnlinkMessage;
import com.dingzhuo.compute.engine.message.calculation.UnloadCalcIndexMessage;
import com.dingzhuo.compute.engine.message.save.SaveMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterTimeMessage;
import com.dingzhuo.compute.engine.message.timer.RegisterType;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.CacheService;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.greenpineyu.fel.context.FelContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component("indexCalcActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalculationIndexActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "indexCalcActor";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final CacheService cacheService;
  private ActorSelection calculateActor;
  private ActorSelection timerActor;
  private ActorSelection saveActor;

  public CalculationIndexActor(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    calculateActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    timerActor = getContext().actorSelection(ActorUtil.getActorAddress(TimerActor.ACTOR_NAME));
    saveActor = getContext().actorSelection(ActorUtil.getActorAddress(SavePeriodActor.ACTOR_NAME));
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof LoadCalcIndexMessage) {
      IndexStorage storage = ((LoadCalcIndexMessage) message).getIndexStorage();
      loadIndex(storage);
    } else if (message instanceof UnloadCalcIndexMessage) {
      String actorId = ((UnloadCalcIndexMessage) message).getActorId();
      unloadIndex(actorId);
    } else if (message instanceof LinkMessage) {
      LinkMessage linkMessage = (LinkMessage) message;
      cacheService.cachePostIndex(linkMessage.getActorId(), linkMessage.getPostActorId());
    } else if (message instanceof UnlinkMessage) {
      UnlinkMessage unlinkMessage = (UnlinkMessage) message;
      cacheService.removePostIndexCache(unlinkMessage.getActorId(), unlinkMessage.getPostActorId());
    } else if (message instanceof CalculateMessage) {
      calculate((CalculateMessage) message);
    }
  }

  private void calculate(CalculateMessage message) {
    try {
      IndexStorage indexStorage = cacheService.getIndexStorageCache(message.getActorId());
      if (StringUtils.isBlank(indexStorage.getCalcText())) {
        return;
      }

      List<String> postActorIds = cacheService.getPostActorIds(message.getActorId());
      FelContext calcContext = FunctionEngine.getInstance().getContext();
      calcContext.set("timeType", message.getTimeType().name());
      calcContext.set("timeCode", message.getTimeCode());
      Object value = FunctionEngine.getInstance().eval(indexStorage.getCalcText(), calcContext);
      SaveMessage saveMessage = new SaveMessage();
      saveMessage.setIndexId(indexStorage.getIndexId());
      saveMessage.setTimeType(message.getTimeType());
      saveMessage.setTimeCode(message.getTimeCode());
      if (value != null) {
        saveMessage.setValue(Double.parseDouble(String.valueOf(value)));
      } else {
        saveMessage.setValue(0d);
      }
      saveMessage.getPostActorIds().addAll(postActorIds);

      saveActor.tell(saveMessage, getSelf());
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void unloadIndex(String actorId) {
    IndexStorage indexStorage = cacheService.getIndexStorageCache(actorId);
    RegisterTimeMessage message =
        new RegisterTimeMessage(actorId, indexStorage.getTimeType(), RegisterType.UNREGISTER);
    timerActor.tell(message, getSelf());
    indexStorage.getParamIndex().forEach(param -> {
      String preActorId = ActorUtil.buildActorId(param, indexStorage.getTimeType());
      calculateActor.tell(new UnlinkMessage(preActorId, actorId), getSelf());
    });
    cacheService.removeIndexCache(actorId);
  }

  private void loadIndex(IndexStorage storage) {
    cacheService.cacheIndexStorage(storage);
    String actorId = ActorUtil.buildActorId(storage);
    storage.getParamIndex().forEach(param -> {
      String preActorId = ActorUtil.buildActorId(param, storage.getTimeType());
      calculateActor.tell(new LinkMessage(preActorId, actorId), getSelf());
    });

    RegisterTimeMessage message =
        new RegisterTimeMessage(actorId, storage.getTimeType(), RegisterType.REGISTER);
    timerActor.tell(message, getSelf());
  }
}
