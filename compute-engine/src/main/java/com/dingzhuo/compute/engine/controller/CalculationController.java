package com.dingzhuo.compute.engine.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.dingzhuo.compute.engine.actor.indexcalc.CalculationIndexActor;
import com.dingzhuo.compute.engine.message.ExecuteType;
import com.dingzhuo.compute.engine.message.calculation.CalculateMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.compute.engine.utils.SpringAkkaExtension;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.model.service.IIndexStorageService;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/computing")
public class CalculationController {

  private final ActorSystem actorSystem;
  private final SpringAkkaExtension akkaExt;
  private final IIndexStorageService storageService;
  private ActorSelection calculateActor;

  public CalculationController(ActorSystem actorSystem, SpringAkkaExtension akkaExt,
      IIndexStorageService storageService) {
    this.actorSystem = actorSystem;
    this.akkaExt = akkaExt;
    this.storageService = storageService;
  }

  @GetMapping("/recalc")
  public AjaxResult reCalc(@RequestBody List<Recalc> recalcList) {
    calculateActor = actorSystem.actorSelection(
        ActorUtil.getActorAddress(CalculationIndexActor.ACTOR_NAME));
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    recalcList.forEach(recalc -> {
      IndexStorage indexStorage = storageService.getIndexStorage(recalc.getIndexId(),
          recalc.getTimeType());
      if (indexStorage != null) {
        String actorId = ActorUtil.buildActorId(indexStorage);
        String timeCode = TimeManager.getTimeCode(
            DateTime.parse(recalc.getDataTime(), fmt).toDate(),
            recalc.getTimeType());
        calculateActor.tell(
            new CalculateMessage(actorId, timeCode, recalc.getTimeType(), ExecuteType.TIMER),
            ActorRef.noSender());
      }
    });
    return AjaxResult.success();
  }
}
