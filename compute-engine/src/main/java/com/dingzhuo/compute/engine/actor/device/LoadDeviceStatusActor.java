package com.dingzhuo.compute.engine.actor.device;

import akka.actor.ActorSelection;
import akka.actor.UntypedAbstractActor;
import com.dingzhuo.compute.engine.message.device.LoadDeviceStatusMessage;
import com.dingzhuo.compute.engine.message.device.UnloadDeviceStatusMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceFormulaService;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

@Component("loadDeviceStatusActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoadDeviceStatusActor extends UntypedAbstractActor {

  public static final String ACTOR_NAME = "loadDeviceStatusActor";
  private final IDeviceFormulaService deviceFormulaService;
  Map<String, DeviceFormula> loadedDeviceFormula = new HashMap<>();
  private ActorSelection deviceStatusTimerActor;

  public LoadDeviceStatusActor(
      IDeviceFormulaService deviceFormulaService) {
    this.deviceFormulaService = deviceFormulaService;
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();

    deviceStatusTimerActor = getContext()
        .actorSelection(ActorUtil.getActorAddress(DeviceStatusTimerActor.ACTOR_NAME));
    this.context().system().scheduler()
        .scheduleAtFixedRate(Duration.Zero(), Duration.create(5, TimeUnit.MINUTES), this.self(),
            Message.REFRESH, this.context().system().dispatcher(), null);
    initDeviceStatus();
    refreshDeviceStatusSetting();
  }

  private void initDeviceStatus() {
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof Message) {
      if (message == Message.REFRESH) {
        refreshDeviceStatusSetting();
      } else {
        this.unhandled(message);
      }
    }
  }

  private void refreshDeviceStatusSetting() {
    List<DeviceFormula> formulas = deviceFormulaService.getAllDeviceFormula();
    Map<String, DeviceFormula> newFormulas = formulas.stream()
        .collect(Collectors.toMap(DeviceFormula::getId, item -> item));

    Set<String> needInstall = new HashSet<>();
    Set<String> needUninstall = new HashSet<>();

    loadedDeviceFormula.forEach((id, alarmItem) -> {
      if (!newFormulas.containsKey(id)) {
        needUninstall.add(id);
      } else {
        Date nowUpdate = newFormulas.get(id).getUpdateTime();
        Date lastUpdate = alarmItem.getUpdateTime();
        if (lastUpdate != null && nowUpdate != null && lastUpdate.after(nowUpdate)) {
          needUninstall.add(id);
          needInstall.add(id);
        }
      }
    });

    newFormulas.forEach((id, formula) -> {
      if (!loadedDeviceFormula.containsKey(id)) {
        needInstall.add(id);
      }
    });

    needUninstall.forEach(id -> {
      DeviceFormula deviceFormula = loadedDeviceFormula.get(id);
      loadedDeviceFormula.remove(id);
      deviceStatusTimerActor.tell(new UnloadDeviceStatusMessage(deviceFormula), getSelf());
    });

    needInstall.forEach(id -> {
      DeviceFormula deviceFormula = newFormulas.get(id);
      deviceStatusTimerActor.tell(new LoadDeviceStatusMessage(deviceFormula), getSelf());
      loadedDeviceFormula.put(id, deviceFormula);
    });
  }

  public enum Message {
    /**
     * 检测配置
     */
    REFRESH
  }
}
