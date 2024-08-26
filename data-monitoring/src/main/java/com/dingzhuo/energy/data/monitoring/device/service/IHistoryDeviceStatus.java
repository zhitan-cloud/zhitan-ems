package com.dingzhuo.energy.data.monitoring.device.service;

import com.dingzhuo.energy.data.monitoring.device.domain.HistoryDeviceStatus;

import java.util.Date;
import java.util.List;

public interface IHistoryDeviceStatus {
  List<HistoryDeviceStatus> getHistoryDeviceStatus(Date beginTime, Date endTime);

  List<HistoryDeviceStatus> getHistoryDeviceStatus(Date beginTime, Date endTime, String status);

}
