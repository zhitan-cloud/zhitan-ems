package com.zhitan.alarm.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.JkRealTimeAlarmList;
import com.zhitan.alarm.domain.RealTimeAlarm;
import com.zhitan.alarm.mapper.RealtimeAlarmMapper;
import com.zhitan.alarm.services.IRealtimeAlarmService;
import com.zhitan.common.enums.TimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  @Override
  public Page<JkRealTimeAlarmList> selectRealtimeAlarmJkPage(JkRealTimeAlarmList jkRealTimeAlarmList, Long pageNum, Long pageSize) {
    return realtimeAlarmMapper.selectRealtimeAlarmJkPage(jkRealTimeAlarmList,new Page<>(pageNum,pageSize));
  }
}
