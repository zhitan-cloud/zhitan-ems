package com.dingzhuo.energy.data.monitoring.device.service;

import com.dingzhuo.energy.data.monitoring.device.domain.RealtimeDeviceStatus;

import java.util.List;

public interface IRealtimeDeviceStatus {
  List<RealtimeDeviceStatus> getRealtimeDeviceStatus();
}
