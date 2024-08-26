package com.dingzhuo.energy.data.monitoring.alarm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;
import com.dingzhuo.energy.data.monitoring.alarm.domain.RealTimeAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.mapper.AlarmItemMapper;
import com.dingzhuo.energy.data.monitoring.alarm.mapper.RealtimeAlarmMapper;
import com.dingzhuo.energy.data.monitoring.alarm.service.IAlarmItemService;
import com.dingzhuo.energy.data.monitoring.alarm.service.IRealtimeAlarmService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtimeAlarmServiceImpl implements IRealtimeAlarmService {

  @Autowired
  private RealtimeAlarmMapper realtimeAlarmMapper;

  @Override
  public List<RealTimeAlarm> getRealTimeAlarm() {
    return realtimeAlarmMapper.getRealTimeAlarm();
  }

  @Override
  public List<RealTimeAlarm> getRealTimeAlarm(TimeType timeType) {
    return null;
  }

  @Override
  public List<RealTimeAlarm> getRealTimeAlarm(String alarmLevel) {
    return null;
  }

  /**
   * @param jkRealTimeAlarmList 实时报警监控用列表
   * @return
   */
  @Override
  public List<JkRealTimeAlarmList> selectRealtimeAlarmJkList(
      JkRealTimeAlarmList jkRealTimeAlarmList) {
    return realtimeAlarmMapper.selectRealtimeAlarmJkList(jkRealTimeAlarmList);
  }

  @Override
  public void insert(RealTimeAlarm realTimeAlarm) {
    realtimeAlarmMapper.insertRealtimeAlarm(realTimeAlarm);
  }

  @Override
  public RealTimeAlarm getRealTimeAlarmByAlarmCode(String alarmCode) {
    return realtimeAlarmMapper.getRealTimeAlarmByAlarmCode(alarmCode);
  }

  @Override
  public RealTimeAlarm getAlarmByItemIdAndTimeCode(String itemId, String timeCode) {
    return realtimeAlarmMapper.getAlarmByItemIdAndTimeCode(itemId, timeCode);
  }
}
