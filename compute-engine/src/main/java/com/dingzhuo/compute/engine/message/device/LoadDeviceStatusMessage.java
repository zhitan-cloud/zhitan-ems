package com.dingzhuo.compute.engine.message.device;

import com.dingzhuo.compute.engine.message.BaseActorMessage;
import com.dingzhuo.compute.engine.utils.ActorUtil;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;

public class LoadDeviceStatusMessage extends BaseActorMessage {

  private DeviceFormula deviceFormula;

  public LoadDeviceStatusMessage(DeviceFormula deviceFormula) {
    super(ActorUtil.buildActorId(deviceFormula));
  }

  public DeviceFormula getDeviceFormula() {
    return deviceFormula;
  }

  public void setDeviceFormula(
      DeviceFormula deviceFormula) {
    this.deviceFormula = deviceFormula;
  }
}
