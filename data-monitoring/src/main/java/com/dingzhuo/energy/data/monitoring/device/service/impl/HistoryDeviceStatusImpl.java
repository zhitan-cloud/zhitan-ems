package com.dingzhuo.energy.data.monitoring.device.service.impl;

import com.dingzhuo.energy.data.monitoring.device.domain.HistoryDeviceStatus;
import com.dingzhuo.energy.data.monitoring.device.service.IHistoryDeviceStatus;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistoryDeviceStatusImpl implements IHistoryDeviceStatus {
  @Override
  public List<HistoryDeviceStatus> getHistoryDeviceStatus(Date beginTime, Date endTime) {
    return null;
  }

  @Override
  public List<HistoryDeviceStatus> getHistoryDeviceStatus(Date beginTime, Date endTime, String status) {
    return null;
  }
}
